package CorrelationID;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "emails";

    public static void main(String[] argv) {

        ConnectionFactory factory = new ConnectionFactory();
        //TODO: Get ip:port from config file
        factory.setHost("localhost");
        Connection connection = null;
        try {
            //set up connection
            connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            //declare queue
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            //attempt to split work between workers evenly
            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            //what the server should do when it receives a message, will be added to channel.basicConsume later
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //set properties according to the message received
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    System.out.println("Responding to corrID: "+ properties.getCorrelationId());

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        System.out.print(message);
                        response = "What I'll be getting from running my application";
                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    } finally {
                        //how and where and what to reply
                        channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        // RabbitMq consumer worker thread notifies the RPC server owner thread
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
        }
    }
}
