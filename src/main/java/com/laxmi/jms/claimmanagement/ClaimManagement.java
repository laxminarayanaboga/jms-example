package com.laxmi.jms.claimmanagement;

import com.laxmi.jms.hospitalmanagement.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class ClaimManagement {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue claimQueue = (Queue) initialContext.lookup("queue/claimQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {
            JMSProducer producer = jmsContext.createProducer();

            Claim claim = new Claim(11, "thisIsDoctorName", "thisIsDoctorType", "ThisIsInsuranceProvider", 1234);
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setObject(claim);
            objectMessage.setIntProperty("hospitalId", 1);

            producer.send(claimQueue, objectMessage);

            // Consumer -- Filtration
            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, "hospitalId=1");
            Claim receivedClaim = consumer.receiveBody(Claim.class);
            System.out.println("receivedClaim -- " + receivedClaim);


        }

    }
}
