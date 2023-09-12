package com.laxmi.jms.hospitalmanagement.clinicals;

import com.laxmi.jms.hospitalmanagement.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class ClicicalsApp {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {
            JMSProducer producer = jmsContext.createProducer();
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            Patient patient = new Patient(123, "BOB", "Blue Cross Blue Shield", 30d, 500d);
            objectMessage.setObject(patient);

            for (int i = 0; i < 10; i++) {
                System.out.println("Sending the patient info to queue.." + i);
                producer.send(requestQueue, objectMessage);
                System.out.println("Sent .. " + i);
            }

            System.out.println("Waiting for the eligibility response ... ");
            MapMessage messageReceived = (MapMessage) jmsContext.createConsumer(replyQueue).receive(30000);
            System.out.println("Is patient eligible -- " + messageReceived.getBoolean("eligible"));

        }

    }
}
