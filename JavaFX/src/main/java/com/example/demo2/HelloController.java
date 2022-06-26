package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;



public class HelloController {



    private String API = "http://localhost:8080/invoices/";

    @FXML
    private TextField tf_id;

    @FXML
    private Text responseTxt;

    @FXML
    private Text createdTS;





    @FXML
    private Button downloadBtn;




    @FXML
    private Label welcomeText;


    //Btn to start POST req.
    @FXML
    void btnClicked(ActionEvent event) throws URISyntaxException, IOException, InterruptedException {

        String id = tf_id.getText();

        String postInvoice = API + id;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(postInvoice))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());



    }

    //Btn to start GET req.
    @FXML
    void getBtnClicked(ActionEvent event) throws URISyntaxException, IOException, InterruptedException {

        Stage mainWindow = (Stage) tf_id.getScene().getWindow();
        String id = tf_id.getText();
        mainWindow.setTitle(id);

        String getInvoice = API + id;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(getInvoice))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());


        //get path of project
        // String projpath = System.getProperty("user.dir") + "\\PDFGenerator" + "\\invoices" + "\\Invoice-"+ tf_id.getText() + ".pdf";

        // Get Project Path
        String basePath = new File("").getAbsoluteFile().getParentFile().getAbsolutePath();

        String fileName = String.format("Invoice-%s.pdf",tf_id.getText());
        String projpath = basePath + "\\PDFGenerator" + "\\invoices\\" + fileName;


        File file = new File(projpath);



        responseTxt.setVisible(true);
        responseTxt.setText("404 not found, wait a moment...");
        createdTS.setVisible(false);
        downloadBtn.setVisible(false);



        //creating scheduled task that tries to find the file every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //check if file exists
                if (file.exists()) {
                    System.out.println("found");
                    Path filepath = Paths.get(projpath);
                    BasicFileAttributes attr;
                    try {
                        //get attr of invoice
                        attr = Files.readAttributes(
                                filepath, BasicFileAttributes.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //show download btn and creation time
                    responseTxt.setVisible(false);
                    createdTS.setVisible(true);
                    createdTS.setText("Creationtime: " + formatDateTime(attr.lastModifiedTime()));
                    downloadBtn.setVisible(true);
                    timer.cancel(); //stops scheduled task
                }
            }
        }, 500, 1000);


    }




    @FXML
    void openFile(ActionEvent event) throws Exception {

        //String projpath = System.getProperty("user.dir") + "\\PDFGenerator" + "\\invoices" + "\\Invoice-"+ tf_id.getText() + ".pdf";


        // Get Project Path
        String basePath = new File("").getAbsoluteFile().getParentFile().getAbsolutePath();

        String fileName = String.format("Invoice-%s.pdf",tf_id.getText());
        String projpath = basePath + "\\PDFGenerator" + "\\invoices\\" + fileName;


        File file = new File(projpath);

        //Open pdf
        Desktop.getDesktop().open(file);






    }






    //https://mkyong.com/java/how-to-format-filetime-in-java/
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    public static String formatDateTime(FileTime fileTime) {

        LocalDateTime localDateTime = fileTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.format(DATE_FORMATTER);
    }

}
