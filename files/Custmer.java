package com.hh.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Custmer {
	public static void main(String[] args) throws JMSException {
		final ConnectionFactory connFactory = new ActiveMQConnectionFactory("tcp://192.168.0.109:61616");

		final Connection conn = connFactory.createConnection();
		conn.start();
		final Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		final Destination dest = sess.createQueue("SampleQueue");
		final MessageConsumer mc = sess.createConsumer(dest);

		mc.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage tm = (TextMessage) message;
				try {
					System.out.println(tm.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

}
