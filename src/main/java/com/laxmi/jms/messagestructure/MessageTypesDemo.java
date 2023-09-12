package com.laxmi.jms.messagestructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;


public class MessageTypesDemo {

    public static void main(String[] args) throws Exception {

        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()
        ) {

//            demoBytesMessage(jmsContext, queue);

//            demoStreamMessage(jmsContext, queue);

//            demoMapMessage(jmsContext, queue);

//            demoObjectMessage(jmsContext, queue);

            demoObjectMessageV2(jmsContext, queue);


        }


    }

    private static void demoObjectMessage(JMSContext jmsContext, Queue queue) throws Exception {
        System.out.println("---- demoObjectMessage ---- ");
        JMSProducer producer = jmsContext.createProducer();
        ObjectMessage objectMessage = jmsContext.createObjectMessage();
        objectMessage.setObject(new Patient(11, "Johnjjjjjj"));
        producer.send(queue, objectMessage);

        ObjectMessage messageReceived = (ObjectMessage) jmsContext.createConsumer(queue).receive(5000);
        System.out.println("messageReceived -- " + messageReceived.getObject());
    }

    private static void demoObjectMessageV2(JMSContext jmsContext, Queue queue) throws Exception {
        System.out.println("---- demoObjectMessageV2 ---- ");
        JMSProducer producer = jmsContext.createProducer();
        producer.send(queue, new Patient(561, "DemoDeasdfasdf asdfasdf"));

        Patient patientReceived = jmsContext.createConsumer(queue).receiveBody(Patient.class, 2000);
        System.out.println("messageReceived -- " + patientReceived);
    }

    private static void demoMapMessage(JMSContext jmsContext, Queue queue) throws Exception {
        System.out.println("---- demoMapMessage ---- ");
        JMSProducer producer = jmsContext.createProducer();
        MapMessage mapMessage = jmsContext.createMapMessage();
        mapMessage.setBoolean("isCreditAvailable", true);
        mapMessage.setDouble("availableCredit", 456789);
        producer.send(queue, mapMessage);

        MapMessage messageReceived = (MapMessage) jmsContext.createConsumer(queue).receive(5000);
        System.out.println("messageReceived -- " + messageReceived.getBoolean("isCreditAvailable"));
        System.out.println("messageReceived -- " + messageReceived.getDouble("availableCredit"));
    }

    private static void demoStreamMessage(JMSContext jmsContext, Queue queue) throws Exception {
        System.out.println("---- demoStreamMessage ---- ");
        JMSProducer producer = jmsContext.createProducer();
        StreamMessage streamMessage = jmsContext.createStreamMessage();
        streamMessage.writeBoolean(true);
        streamMessage.writeLong(234l);
        producer.send(queue, streamMessage);

        StreamMessage messageReceived = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
        System.out.println("messageReceived -- " + messageReceived.readBoolean());
        System.out.println("messageReceived -- " + messageReceived.readLong());
    }

    private static void demoBytesMessage(JMSContext jmsContext, Queue queue) throws Exception {
        System.out.println("---- demoBytesMessage ---- ");
        JMSProducer producer = jmsContext.createProducer();
        BytesMessage bytesMessage = jmsContext.createBytesMessage();
        bytesMessage.writeUTF("abcd");
        bytesMessage.writeLong(123l);
        producer.send(queue, bytesMessage);

        BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive(5000);
        System.out.println("messageReceived -- " + messageReceived.readUTF());
        System.out.println("messageReceived -- " + messageReceived.readLong());
    }


}
