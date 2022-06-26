package at.technikum;

import at.technikum.service.ConsumptionService;

public class Main {
    private final static String BROKER_URL = "tcp://localhost:61616";

    public static void main(String[] args) {
        ConsumptionService consumptionService = new ConsumptionService("VerbrauchsabfragenQueue", "VerbrauchsInfoQueue", BROKER_URL);
        consumptionService.run();
    }
}