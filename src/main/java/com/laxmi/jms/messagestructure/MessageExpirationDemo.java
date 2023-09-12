package com.laxmi.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;


public class MessageExpirationDemo {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        Queue expiryQueue = (Queue) initialContext.lookup("queue/ExpiryQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()
        ) {
            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(2000);
            producer.send(queue, "Hello World! -- ");

            Thread.sleep(2100);
            TextMessage messageReceived = (TextMessage)jmsContext.createConsumer(queue).receive(5000);
            System.out.println("messageReceived -- " + messageReceived);

            TextMessage expiryQueueMessage = (TextMessage)jmsContext.createConsumer(expiryQueue).receive(5000);
            System.out.println("expiryQueueMessage -- " + expiryQueueMessage);
        }


    }

}
