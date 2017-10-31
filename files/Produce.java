package com.hh.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Hello world!
 *
 */
public class Produce 
{
	 public static void main(final String[] args) throws Exception {
		    final ConnectionFactory connFactory = new ActiveMQConnectionFactory("tcp://192.168.0.109:61616");
		 
		    final Connection conn = connFactory.createConnection();
		 
		    final Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		 
		    final Destination dest = sess.createQueue("SampleQueue");
		 
		    final MessageProducer prod = sess.createProducer(dest);
		 
		    final Message msg = sess.createTextMessage("Simples Assim2");
		 
		    prod.send(msg);
		 
		    conn.close();
		  }
}
