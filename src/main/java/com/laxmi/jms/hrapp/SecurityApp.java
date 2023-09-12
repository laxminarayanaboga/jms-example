package com.laxmi.jms.hrapp;

import com.laxmi.jms.hrapp.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class SecurityApp {
    public static void main(String[] args) throws Exception {

        // Demonstrates the durableConsumer

        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {
            jmsContext.setClientID("securityApp");
            JMSConsumer consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            System.out.println("Subscribed to topic using clientId - " + "securityApp" + " & subscription as " + "subscription1");
            consumer.close();
            System.out.println("Closed the consumer");

            System.out.println("Shutting down for 10 sec during which the messages will be published");
            Thread.sleep(10000);

            System.out.println("Starting the consumer again");
            consumer = jmsContext.createDurableConsumer(topic, "subscription1");
            for (int i = 1; i <= 10; i++) {

                Message message = consumer.receive();
                Employee employeeReceived = message.getBody(Employee.class);
                System.out.println("SecurityApp -- employeeReceived -- " + employeeReceived);
            }
            consumer.close();
            jmsContext.unsubscribe("subscription1");
        }
    }
}
