package com.fove.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory connectionFactory;
        Connection connection = null; 
        //一次发送的会话
        Session session; 
        //消费者
        Destination destination; 
        //生产者
        MessageProducer producer;
        //使用缺省的账户获取工厂对象
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
        try {
            connection = connectionFactory.createConnection();
            //启动
            connection.start();
            //Parameter1 表示是否支持事务
            //Parameter2：acknowledgment mode
            //Session.AUTO_ACKNOWLEDGE：当客户端从 receive 或 onMessage成功返回时，Session 自动签收客户端的这条消息的收条。
            //Session.CLIENT_ACKNOWLEDGE： 客户端通过调用消息的 acknowledge 方法签收消息
            //Session.DUPS_OK_ACKNOWLEDGE:一旦消息处理中返回了应用程序接收方法，Session对象即确认消息接收，允许重复确认
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("fove");
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            for (int i = 1; i <= 10; i++) {
                TextMessage message = session.createTextMessage("消息" + i);
                System.out.println("已生产"+i+"条消息!");
                producer.send(message);
            }
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}