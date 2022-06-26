package at.technikum;

import at.technikum.service.InvoiceGenService;

public class Main {
    private final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        InvoiceGenService invoiceGenService = new InvoiceGenService("invoiceQueue", "", BROKER_URL);
        invoiceGenService.run();
    }
}