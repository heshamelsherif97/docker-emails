package CommandPattern.Emails.Controller;

import com.couchbase.client.deps.io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ControllerHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private String requestBody;
    private long correlationId;
    volatile String responseBody;
    ExecutorService executorService = Executors.newCachedThreadPool();


    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        ctx.fireChannelReadComplete();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //System.out.println("Message  " + msg);
        if (msg instanceof HttpRequest) {
//            ctx.write(msg);
            HttpRequest request = this.request = (HttpRequest) msg;


            if (HttpUtil.is100ContinueExpected(request)) {
                System.out.println("in weird 100 thing");
                send100Continue(ctx);
            }

        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();

            System.out.println("content is " + content.toString(CharsetUtil.UTF_8));
//            setRequestBody(content.toString(CharsetUtil.UTF_8));
            ctx.fireChannelRead(content.copy());
        }
        if (msg instanceof LastHttpContent) {
//            LastHttpContent trailer = (LastHttpContent) msg;
            HttpObject trailer = (HttpObject) msg;
//            writeresponse(trailer, ctx);
        }
    }


    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

