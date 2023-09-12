package com.laxmi.jms.hospitalmanagement.eligibilitycheck;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class EligibilityCheckerApp {
    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {
            System.out.println("ElibilityCheckerApp started");
            JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            consumer.setMessageListener(new EligibilityCheckLister());

//           System.out.println(consumer);

            Thread.sleep(15000);
            System.out.println("EligibilityCheckerApp completed!");
        }

    }
}
