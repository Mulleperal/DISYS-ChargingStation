package at.technikum;

import at.technikum.service.ConsumptionService;
import at.technikum.service.JobInfoService;

public class Main {
    private final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        JobInfoService jobInfoService = new JobInfoService("jobInfoQueue","", BROKER_URL);
        ConsumptionService consumptionService = new ConsumptionService("VerbrauchsInfoQueue", "invoiceQueue", BROKER_URL);
        consumptionService.run();
    }
}