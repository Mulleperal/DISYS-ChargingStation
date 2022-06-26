package at.technikum.service;


import at.technikum.communication.Consumer;

public abstract class BaseService implements Runnable {

    private final String inDestination;
    private final String outDestination;
    private final String brokerUrl;

    public BaseService(String inDestination, String outDestination, String brokerUrl) {
        this.inDestination = inDestination;
        this.outDestination = outDestination;
        this.brokerUrl = brokerUrl;
    }

    @Override
    public void run() {
        while (true) {
            execute(inDestination, outDestination, brokerUrl);
        }
    }

    public void execute(String inDestination, String outDestination, String brokerUrl) {
        String input = Consumer.receive(inDestination, 10000, brokerUrl);

        if (null == input) {
            return;
        }

        executeInternal(input);
    }

    protected abstract String executeInternal(String input);
}
