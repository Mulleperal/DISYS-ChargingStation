package at.technikum.communication;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import javax.jms.*;

public class Consumer {
    public static String receive(String queueName, long timeout, String brokerUrl) {
        // taken from: https://activemq.apache.org/hello-world

        String returnValue = null;
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            var policy = new ActiveMQPrefetchPolicy();
            policy.setAll(0);
            factory.setPrefetchPolicy(policy);

            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);

            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive(timeout);
            if(message instanceof  TextMessage){
                returnValue = ((TextMessage)message).getText();
            }

            consumer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

        return returnValue;
    }
}
