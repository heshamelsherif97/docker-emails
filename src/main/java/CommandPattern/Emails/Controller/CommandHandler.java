package CommandPattern.Emails.Controller;

import CommandPattern.Command;
import com.rabbitmq.client.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;




import static io.netty.buffer.Unpooled.copiedBuffer;


public class CommandHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        //System.out.println("JSON HANDLER");
        ControlCommandMap.instantiate();
        ByteBuf buffer = (ByteBuf) o;
        String command;
        try {
            JSONObject jsonObject = new JSONObject(buffer.toString(CharsetUtil.UTF_8));
            command = jsonObject.getString("command");
            System.out.println(command);
            JSONObject response = new JSONObject();
            Command x  = (Command) ControlCommandMap.queryClass(command).newInstance();
            response = x.execute(jsonObject);

            FullHttpResponse response1 = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    copiedBuffer(response.toString().getBytes())
            );
            channelHandlerContext.writeAndFlush(response1);
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println(" Exception : " + buffer.toString(CharsetUtil.UTF_8));

           /* byte[] bytes = new byte[buffer.capacity()];
            buffer.getBytes(0, bytes);
            Class<?> newClass = (new DynamicClassLoader()).createClass(bytes, "Habiiba");*/

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
