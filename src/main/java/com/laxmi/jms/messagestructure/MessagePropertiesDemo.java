package com.laxmi.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;


public class MessagePropertiesDemo {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()
        ) {
            JMSProducer producer = jmsContext.createProducer();
            TextMessage textMessage = jmsContext.createTextMessage("Hello World! 1");
            textMessage.setBooleanProperty("loggedIn", true);
            textMessage.setStringProperty("userId", "test123");

            producer.send(queue, textMessage);
            TextMessage messageReceived = (TextMessage) jmsContext.createConsumer(queue).receive(5000);
            System.out.println("messageReceived -- " + messageReceived.getText());
            System.out.println("messageReceived -- getStringProperty " + messageReceived.getStringProperty("userId"));
            System.out.println("messageReceived -- getBooleanProperty " + messageReceived.getBooleanProperty("loggedIn"));


        }


    }

}
