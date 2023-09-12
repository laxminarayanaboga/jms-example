package com.laxmi.jms.hrapp;

import com.laxmi.jms.hrapp.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class PayrollApp {
    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {
            JMSConsumer consumer = jmsContext.createConsumer(topic);

            for (int i = 1; i <= 10; i++) {
                Message message = consumer.receive();
                Employee employeeReceived = message.getBody(Employee.class);
                System.out.println("PayrollApp -- employeeReceived -- " + employeeReceived);
            }

        }
    }
}
