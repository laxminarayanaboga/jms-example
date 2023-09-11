package com.laxmi.jms.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {

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

            TextMessage message = session.createTextMessage("This is message 1");
            producer.send(message);

            TextMessage message2 = session.createTextMessage("This is message 2");
            producer.send(message2);

            QueueBrowser browser = session.createBrowser(queue);

            Enumeration messagesEnum = browser.getEnumeration();

            while (messagesEnum.hasMoreElements()) {
                TextMessage tempMessage = (TextMessage) messagesEnum.nextElement();
                System.out.println("Browsing --> " + tempMessage.getText());
            }

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("Message received --> " + messageReceived.getText());

            messageReceived = (TextMessage) consumer.receive(5000);
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
