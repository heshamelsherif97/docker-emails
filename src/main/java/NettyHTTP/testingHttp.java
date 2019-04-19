package NettyHTTP;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.concurrent.ExecutionException;

public class testingHttp {
    public static void sendReq(){

    }

    public static void main(String[] args) {
        Runnable myRunnable =
                new Runnable(){
                    public void run() {
                        String payload = "{" +
                                "\"service\": \"users\", " +
                                "\"method\": \"fib\"" +
                                "}";
                        StringEntity entity = new StringEntity(payload,
                                ContentType.APPLICATION_FORM_URLENCODED);

                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpPost request = new HttpPost("http://localhost:8083/");
                        request.setEntity(entity);
                        try {
                            httpClient.execute(request);
                        }
                        catch (Exception e){

                        }
                    }
                };

        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
    }
}
