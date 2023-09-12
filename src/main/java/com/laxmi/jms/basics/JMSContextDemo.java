package com.laxmi.jms.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;


public class JMSContextDemo {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()
        ) {
            jmsContext.createProducer().send(queue, "Hello World!");

            String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);
            System.out.println("messageReceived -- " + messageReceived);
        }


    }

}
