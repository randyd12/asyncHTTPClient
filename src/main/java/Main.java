

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;

// Note:
// To compile use Maven:  compile and package
// For the test to work you need an REST endpoing living at the place where this code is pointed
// To run from the command line:
// D:\src\github\randyd12\asyncHTTPClient\target>java -jar asyncHTTPClient-1-jar-with-dependencies.jar
//


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            Future<Boolean> future = httpclient.execute(
                    HttpAsyncMethods.createGet("http://localhost:8081/v1.0/ping"),
                    new MyResponseConsumer(), null);
            Boolean result = future.get();
            if (result != null && result.booleanValue()) {
                System.out.println("Request successfully executed");
            } else {
                System.out.println("Request failed");
            }
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");


    }

    static class MyResponseConsumer extends AsyncCharConsumer<Boolean> {

        @Override
        protected void onResponseReceived(final HttpResponse response) {
        }

        @Override
        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
            while (buf.hasRemaining()) {
                System.out.print(buf.get());
            }
        }

        @Override
        protected void releaseResources() {
        }

        @Override
        protected Boolean buildResult(final HttpContext context) {
            return Boolean.TRUE;
        }

    }
}
