package at.technikum;

import at.technikum.service.ConsumptionService;
import at.technikum.service.JobInfoService;

// Threading -> https://www.w3schools.com/java/java_threads.asp
public class Main extends Thread{
    private final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        Main thread = new Main();
        thread.start();
        JobInfoService jobInfoService = new JobInfoService("jobInfoQueue","", BROKER_URL);
        jobInfoService.run();
    }

    public void run() {
        ConsumptionService consumptionService = new ConsumptionService("VerbrauchsInfoQueue", "invoiceQueue", BROKER_URL);
        consumptionService.run();
    }
}