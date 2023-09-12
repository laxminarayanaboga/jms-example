package com.laxmi.jms.hrapp;

import com.laxmi.jms.hrapp.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class HRApp {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/empTopic");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            for (int i = 1; i <= 10; i++) {

                Employee employee = new Employee(i, "thisIsFirstName",
                        "thisIsLastName", "thisis@email.com", "thisIsDesignation", "123456");

                jmsContext.createProducer().send(topic, employee);
                System.out.println("Send Message -- " + employee);
            }
        }
    }
}

