package com.laxmi.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;

public class MessagePriority {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSProducer producer = jmsContext.createProducer();

            String[] messages = new String[3];
            messages[0] = "Message 0";
            messages[1] = "Message 1";
            messages[2] = "Message 2";

            producer.setPriority(3);
            producer.send(queue, messages[0]);

            producer.setPriority(1);
            producer.send(queue, messages[1]);

            producer.setPriority(9);
            producer.send(queue, messages[2]);

            JMSConsumer consumer = jmsContext.createConsumer(queue);

            String messageReceived = consumer.receiveBody(String.class);
            System.out.println("messageReceived -- " + messageReceived);

            messageReceived = consumer.receiveBody(String.class);
            System.out.println("messageReceived -- " + messageReceived);

            messageReceived = consumer.receiveBody(String.class);
            System.out.println("messageReceived -- " + messageReceived);
        }
    }
}
