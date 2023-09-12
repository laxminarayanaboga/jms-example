package com.laxmi.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;


public class RequestReplyDemo {
    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()
        ) {
            // Produce sending a message on requestQueue
            // Expecting a reply from the consumer.. so create a temp queue and set it in the message header..
            JMSProducer requestProducer = jmsContext.createProducer();
            TextMessage message = jmsContext.createTextMessage("sending to requestQueue");
//            Queue replyQueue = jmsContext.createTemporaryQueue();
            message.setJMSReplyTo(replyQueue);
            requestProducer.send(requestQueue, message);
            System.out.println(message.getJMSMessageID());

            // Receiver receive the message
            // Sends some message back to to replyQueue
            JMSConsumer requestConsumer = jmsContext.createConsumer(requestQueue);
            TextMessage messageReceivedFromRequestQueue = (TextMessage) requestConsumer.receive();
            System.out.println("messageReceivedRequestQueue -- " + messageReceivedFromRequestQueue.getText());
            System.out.println("messageReceivedRequestQueue -- Id -- " + messageReceivedFromRequestQueue.getJMSMessageID());

            JMSProducer replyProducer = jmsContext.createProducer();
            TextMessage replyMessage = jmsContext.createTextMessage("Sending to replyQueue");
            replyMessage.setJMSCorrelationID(messageReceivedFromRequestQueue.getJMSMessageID());
            replyProducer.send(messageReceivedFromRequestQueue.getJMSReplyTo(), replyMessage);

            // Producer poll and read the message from the reply queue
            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
            TextMessage messageReceivedInReplyQueue = (TextMessage) replyConsumer.receive();
            System.out.println("messageReceivedReplyQueue -- " + messageReceivedInReplyQueue.getText());
            System.out.println("messageReceivedReplyQueue -getJMSCorrelationID- " + messageReceivedInReplyQueue.getJMSCorrelationID());

        }


    }

}
