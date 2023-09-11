package com.laxmi.jms.basics;

import org.w3c.dom.Text;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World");

        InitialContext initialContext = null;
        initialContext = new InitialContext();

        Topic topic = (Topic) initialContext.lookup("topic/myTopic");

        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();

        MessageProducer producer = session.createProducer(topic);

        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 = session.createConsumer(topic);
        MessageConsumer consumer3 = session.createConsumer(topic);

        TextMessage message = session.createTextMessage("This is sample Text message abcdef");
        producer.send(message);

        connection.start();
        TextMessage message1 = (TextMessage) consumer1.receive();
        System.out.println("message1 received --> " + message1.getText());

        TextMessage message2 = (TextMessage) consumer2.receive();
        System.out.println("message2 received --> " + message2.getText());

        TextMessage message3 = (TextMessage) consumer3.receive();
        System.out.println("message3 received --> " + message3.getText());

        connection.close();
        initialContext.close();



    }
}
