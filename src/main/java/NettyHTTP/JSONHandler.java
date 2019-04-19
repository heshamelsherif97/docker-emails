
package NettyHTTP;

import Redis.Redis;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParser;
import com.rabbitmq.client.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

import static io.netty.buffer.Unpooled.copiedBuffer;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class JSONHandler extends SimpleChannelInboundHandler<Object>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf buffer = (ByteBuf) o;
        JSONObject jsonObject = new JSONObject(buffer.toString(CharsetUtil.UTF_8));
        String responseMessage  = contactMQ(jsonObject);
        JSONObject responseMessageJson= new JSONObject(responseMessage);
        String sessionResult = sessionHandler(responseMessage,jsonObject);
        if(!sessionResult.equals("")){
            responseMessageJson.put("sessionId", sessionResult);
            responseMessage = responseMessageJson.toString();
            Redis.getJedis().set(sessionResult, jsonObject.getString("username"));
        }
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                copiedBuffer(responseMessage.getBytes())
        );
        channelHandlerContext.writeAndFlush(response);
    }

    private String sessionHandler(String responseMessage , JSONObject jsonObject){
        String result = "";
        if(jsonObject.getString("method").equals("login")){
//            System.out.print(responseMessage);
            if(responseMessage.contains("Logged In")){
                UUID sessionID = UUID.randomUUID();
                 result = sessionID.toString();
            }


        }
        return  result;
    }

    protected String contactMQ(JSONObject object)
    {
        try {
            String message = object.toString();
            String requestQueueName = object.getString("service");

            //create connection to MQ, TODO: make mq ip:port variables from config file
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            //send to MQ, TODO: convert queue to exchange?
            String corrId = UUID.randomUUID().toString();
            String replyQueueName = channel.queueDeclare().getQueue();
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();

            channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

            //listen to response, then check against corrID
            BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    response.offer(new String(delivery.getBody(), "UTF-8"));
                }
            };

            String ctag = channel.basicConsume(replyQueueName, true, deliverCallback, consumerTag -> {
            });

            String result = response.take();
            channel.basicCancel(ctag);
            return result;


        } catch (Exception e) {
            System.out.println(e.toString());
            return e.toString();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}