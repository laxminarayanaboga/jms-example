package com.laxmi.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {

    public static void main(String[] args) {
        System.out.println("Hello World");

        InitialContext initialContext = null;

        Connection connection = null;
        ConnectionFactory connectionFactory;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");

            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage("This is sample Text message abcde");
            producer.send(message);
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("Message received --> " + messageReceived.getText());


        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }

        }


    }
}
