package NettyHTTP;

import Redis.Redis;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class authHandler extends SimpleChannelInboundHandler<Object> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf buffer = (ByteBuf) o;
        JSONObject jsonObject = new JSONObject(buffer.toString(CharsetUtil.UTF_8));
        String responseMessage = "";
        boolean flag = true;
        try{
            String sessId = jsonObject.getString("sessionId");
            try{
                String username = jsonObject.getString("username");
                if(Redis.getJedis().get(sessId).equals(username)){
                    channelHandlerContext.fireChannelRead(o);
                }else{
                    responseMessage = "Invalid Session";
                    flag = false;
                }
            }
            catch(Exception f){
                responseMessage = "Invalid Session";
                flag = false;
            }
        }catch (Exception e){
            try {
                String method = jsonObject.getString("method");
                if (method.equals("login")){
                    channelHandlerContext.fireChannelRead(o);
                }else if(method.equals("SignUp")){
                    channelHandlerContext.fireChannelRead(o);
                }
                else{
                    responseMessage = "Not Authorized";
                    flag = false;
                }
            }
            catch(Exception f){
                responseMessage = "No session Found";
                flag = false;
            }
        }

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                copiedBuffer(responseMessage.getBytes())
        );
        if(!flag)
        channelHandlerContext.writeAndFlush(response);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        ctx.fireChannelReadComplete();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
