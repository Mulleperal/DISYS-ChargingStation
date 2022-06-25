package at.technikum.service;

import at.technikum.models.Customer;
import at.technikum.models.InvoiceInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InvoiceGenService extends BaseService {

    private final String id;


    public InvoiceGenService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("PDF Generator Worker (" + this.id + ") started...");
    }

    @Override
    protected String executeInternal(String input) {
        // input (=jsonString) umwandeln
        InvoiceInfo invoiceInfo;
        ObjectMapper mapper = new ObjectMapper();
        try {
            invoiceInfo = mapper.readValue(input, InvoiceInfo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Customer customerData = invoiceInfo.getCustomer();
        HashMap<String, String> consumptions = invoiceInfo.getConsumption();


        // Get Project Path
        String basePath = new File("").getAbsolutePath();

        String fileName = String.format("Invoice-%s.pdf",customerData.getId());
        String filePath = basePath + "\\invoices\\" + fileName;

        //PDF creation
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            //Header
            Paragraph invoiceTitle = new Paragraph("Rechung für Kundennr. " + invoiceInfo.getCustomer().getId())
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(18)
                    .setBold();
            document.add(invoiceTitle);


            // Address
            String addressBlock = customerData.getName() + "\n" +
                    customerData.getStreet() + "\n" +
                    customerData.getPostalCode() + " " + customerData.getCity();

            Paragraph addressData = new Paragraph(addressBlock);
            document.add(addressData);


            //Cost overview
            Paragraph tableHeader = new Paragraph("Kostenaufstellung")
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                    .setFontSize(12)
                    .setBold();
            document.add(tableHeader);

            Table table = new Table(UnitValue.createPercentArray(4))
                    .useAllAvailableWidth();
            table.addHeaderCell(getHeaderCell("Date"));
            table.addHeaderCell(getHeaderCell("Amount [kWh]"));
            table.addHeaderCell(getHeaderCell("kWh-Price"));
            table.addHeaderCell(getHeaderCell("Subtotal"));

            double totalPrice = 0;

            HashMap<String, String> verbrauch = invoiceInfo.getConsumption();
            for (Map.Entry<String,String> entry : verbrauch.entrySet()) {
                String timestamp = entry.getKey();
                int consumption = Integer.parseInt(entry.getValue());

                // Zelle 1 - Timestamp
                Paragraph paraTime = new Paragraph()
                        .setFontSize(10)
                        .add(timestamp);
                table.addCell(new Cell().add(paraTime));

                // Zelle 2 - Consumption in kWh
                Paragraph paraCons = new Paragraph()
                        .setFontSize(10)
                        .add(Integer.toString(consumption));
                table.addCell(new Cell().add(paraCons));

                // Zelle 3 - Price per kWh
                Paragraph paraPrice = new Paragraph()
                        .setFontSize(10)
                        .add("0,45 €/kWh");
                table.addCell(new Cell().add(paraPrice));

                // Zelle 4 - Sum
                double price = consumption * 0.45;
                String temp = String.format("%.2f EUR", price);
                Paragraph paraSum = new Paragraph()
                        .setFontSize(10)
                        .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                        .add(new Tab())
                        .add(String.valueOf(temp));
                table.addCell(new Cell().add(paraSum));

                totalPrice += price;

            }

            table.setFontSize(12).setBackgroundColor(ColorConstants.WHITE);

            table.setMarginBottom(12.5f);// = 12.5f;
            document.add(table);


            //Calculations
            double tax = totalPrice * 0.2;
            double sum = totalPrice + tax;

            String temp_net = String.format("%.2f EUR", totalPrice);
            String temp_tax = String.format("%.2f EUR", tax);
            String temp_sum = String.format("%.2f EUR", sum);

            //Table for net, tax and total
            Table tbl_sum = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

            // Header Cells
            tbl_sum.addHeaderCell(getHeaderCell("Net"));
            tbl_sum.addHeaderCell(getHeaderCell("Tax"));
            tbl_sum.addHeaderCell(getHeaderCell("Total"));


            tbl_sum.setFontSize(12).setBackgroundColor(ColorConstants.WHITE);

            tbl_sum.addCell(getTemplateCellSumContent(String.valueOf(temp_net)));
            tbl_sum.addCell(getTemplateCellSumContent(String.valueOf(temp_tax)));
            tbl_sum.addCell(getTemplateCellSumContent(String.valueOf(temp_sum)));


            document.add(tbl_sum);

            document.close();

            //output path
            System.out.println(filePath);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "DONE";
    }

    private static Cell getHeaderCell(String s) {
        return new Cell().add(new Paragraph(s)).setBold().setBackgroundColor(ColorConstants.GRAY);
    }

    private static Cell getTemplateCellSumContent(String s) {
        Paragraph templateContent = new Paragraph()
                .setFontSize(12)
                .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                .add(new Tab())
                .add(s);
        return new Cell().add(templateContent);
    }

}
