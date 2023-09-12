package com.laxmi.jms.hospitalmanagement.eligibilitycheck;

import com.laxmi.jms.hospitalmanagement.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckLister implements MessageListener {
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            MapMessage replyMessage = jmsContext.createMapMessage();

            Patient patient = (Patient) objectMessage.getObject();
            System.out.println("in Listener -- onMessage. Patient received --  " + patient);

            String insuranceProvider = patient.getInsuranceProvider();
            System.out.println("insuranceProvider -- " + insuranceProvider);

            if(insuranceProvider.equals("Blue Cross Blue Shield") || insuranceProvider.equals("United Health")){
                if(patient.getCopay()<40 && patient.getAmountToBePaid()<1000){
                    replyMessage.setBoolean("eligible", true);
                }
            }else{
                replyMessage.setBoolean("eligible", false);
            }
            JMSProducer producer = jmsContext.createProducer();
            producer.send(replyQueue, replyMessage);
            System.out.println("eligibilty reply message sent -- " + replyMessage);

        } catch (JMSException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
