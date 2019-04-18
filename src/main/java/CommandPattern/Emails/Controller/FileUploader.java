package CommandPattern.Emails.Controller;
import CommandPattern.Command;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.CREATED;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Handles uploading of file and then saves it to a known location.
 */
public class FileUploader extends SimpleChannelInboundHandler<HttpObject> {

    // Factory that writes to disk
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(true);
    private static final String FILE_UPLOAD_LOCN = "/home/vbox/Documents/scalableXmahaba/target/classes/CommandPattern/Emails/";
    private HttpRequest httpRequest;
    private HttpPostRequestDecoder httpDecoder;

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject httpObject)
            throws Exception {
        if (httpObject instanceof HttpRequest) {
            httpRequest = (HttpRequest) httpObject;
            final URI uri = new URI(httpRequest.uri());

           // System.out.println("Got URI " + uri);
            ///home/vbox/Documents/scalableXmahaba
            if (httpRequest.method() == POST) {
                httpDecoder = new HttpPostRequestDecoder(factory, httpRequest);
                httpDecoder.setDiscardThreshold(0);
            } else {
               // sendResponse(ctx, METHOD_NOT_ALLOWED, null);
                ctx.write(httpObject);
                if (HttpUtil.is100ContinueExpected(httpRequest)) {
                  //System.out.println("in weird 100 thing");
                  send100Continue(ctx);
                }
            }

        }

        if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) httpObject;
            ByteBuf content = httpContent.content();

//            System.out.println("content is " + content.toString(com.couchbase.client.deps.io.netty.util.CharsetUtil.UTF_8));
//            setRequestBody(content.toString(CharsetUtil.UTF_8));
            ctx.fireChannelRead(content.copy());
        }
        if (httpObject instanceof LastHttpContent) {
//            LastHttpContent trailer = (LastHttpContent) msg;
            HttpObject trailer = (HttpObject) httpObject;
//            writeresponse(trailer, ctx);
        }
        if (httpDecoder != null) {
            if (httpObject instanceof HttpContent) {
                final HttpContent chunk = (HttpContent) httpObject;
                httpDecoder.offer(chunk);
                final URI uri = new URI(httpRequest.uri());

                System.out.println("Got URI " + uri);
                readChunk(ctx, httpObject, uri);

                if (chunk instanceof LastHttpContent) {
                    resetPostRequestDecoder();
                }
            }
        }
    }

    private void readChunk(ChannelHandlerContext ctx, HttpObject httpObject, URI uri) throws Exception {
        while (httpDecoder.hasNext()) {
            InterfaceHttpData data = httpDecoder.next();
            //System.out.println(".next: " + data);
            if (data != null) {
                try {
                    switch (data.getHttpDataType()) {
                        case Attribute:
                            break;
                        case FileUpload:
                            final FileUpload fileUpload = (FileUpload) data;
                            final File file = new File(FILE_UPLOAD_LOCN + fileUpload.getFilename());

                            String [] url = uri.toString().split("/");
                            //System.out.println("URL is " + Arrays.toString(url));
                            if( url.length != 3 && url[0].equals(""))
                                throw new Exception();

                           //

                            if(file.exists() && url[1].equals("add_command"))
                                throw new Exception();

                            if(!file.exists() && (url[1].equals("delete_command") || url[1].equals("update_command") || url[1].equals("update_class")))
                                throw new Exception();

                            if (file.exists() && (url[1].equals("update_command") || url[1].equals("update_class")))
                            {
                                file.delete();
                                System.out.println("here");
                            }


                            file.createNewFile();

                            JSONObject json = new JSONObject();
                            json.put("file", file);
                            json.put("command_name", url[2]);

                            System.out.println("Created file " + file);
                            try (FileChannel inputChannel = new FileInputStream(fileUpload.getFile()).getChannel();
                                 FileChannel outputChannel = new FileOutputStream(file).getChannel()) {
                                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

                                Command x  = (Command) ControlCommandMap.queryClass(url[1]).newInstance();
                                JSONObject response = x.execute(json);

                                //EditClassesCommands.handleFile(file,url[1], url[2]);
                                sendResponse(ctx, CREATED, "{ \"command\" : \"successful " + url[1] +"\" }");
                            }
                            break;
                    }
                } finally {
                    data.release();
                }
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                CONTINUE);
        ctx.writeAndFlush(response);
    }

    /**
     * Sends a response back.
     * @param ctx
     * @param status
     * @param message
     */
    private static void sendResponse(ChannelHandlerContext ctx, HttpResponseStatus status, String message) {
        final FullHttpResponse response;
        String msgDesc = message;
        if (message == null) {
            msgDesc = "Failure: " + status;
        }
        msgDesc += " \r\n";

        final ByteBuf buffer = Unpooled.copiedBuffer(msgDesc, CharsetUtil.UTF_8);
        if (status.code() >= HttpResponseStatus.BAD_REQUEST.code()) {
            response = new DefaultFullHttpResponse(HTTP_1_1, status, buffer);
        } else {
            response = new DefaultFullHttpResponse(HTTP_1_1, status, buffer);
        }
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the response is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        //ctx.writeAndFlush(response);
    }

    private void resetPostRequestDecoder() {
        httpRequest = null;
        httpDecoder.destroy();
        httpDecoder = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Got exception " + cause);
        ctx.channel().close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (httpDecoder != null) {
            httpDecoder.cleanFiles();
        }
    }

}
