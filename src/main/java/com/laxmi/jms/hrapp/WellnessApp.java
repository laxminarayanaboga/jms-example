package com.laxmi.jms.hrapp;

import com.laxmi.jms.hrapp.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class WellnessApp {

    // Demonstrates the Shared Consumer
    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {
            JMSConsumer consumer1 = jmsContext.createSharedConsumer(topic, "sharedConsumer");
            JMSConsumer consumer2 = jmsContext.createSharedConsumer(topic, "sharedConsumer");


            for (int i = 1; i <= 10; i = i + 2) {
                Message message1 = consumer1.receive();
                Employee employeeReceived1 = message1.getBody(Employee.class);
                System.out.println("WellnessApp -- employeeReceived1 -- " + employeeReceived1);

                Message message2 = consumer2.receive();
                Employee employeeReceived2 = message2.getBody(Employee.class);
                System.out.println("WellnessApp -- employeeReceived2 -- " + employeeReceived2);
            }

        }
    }
}
