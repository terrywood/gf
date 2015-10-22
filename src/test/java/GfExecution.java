import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import java.util.Map;

public class GfExecution {
    public static void main(String[] args) throws Exception {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.

        long c1 = System.currentTimeMillis();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm) .build();
        try {
            // create an array of URIs to perform GETs on
            String[] urisToGet = {
                    "https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878002&dse_sessionId=DCCF5D64389FC474A8B841A3EFDA9109",
                    "https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878003&dse_sessionId=DCCF5D64389FC474A8B841A3EFDA9109"
            };

            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
            String session="Hm_lvt_07e1b3469e412552a15451441d5e3973=1438569747,1439519153; name=value; JSESSIONID=C40E4F8E71E716B866BCC9E95F6FB993; dse_sessionId=DCCF5D64389FC474A8B841A3EFDA9109; userId=*A0*10*B1t*0F*E2*0E*B1*91z*C66*C2*BCa*AEG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";
            for (int i = 0; i < threads.length; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                httpget.addHeader("Cookie",session);
                threads[i] = new GetThread(httpclient, httpget, i + 1);
            }

            // start the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].start();
            }

            // join the threads
            for (int j = 0; j < threads.length; j++) {
                threads[j].join();
            }

            System.out.println(threads[0].getPrice()+threads[1].getPrice());


            System.out.println(System.currentTimeMillis()-c1 );
            System.out.println("end");
        } finally {
            httpclient.close();
        }
    }

    /**
     * A thread that performs a GET.
     */
    static class GetThread extends Thread {
        ObjectMapper  objectMapper = new ObjectMapper();
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;
        private double price;

        public double getPrice(){
            return  price;
        }

        public GetThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }

        /**
         * Executes the GetMethod and prints some status information.
         */
        @Override
        public void run() {
            try {
                System.out.println(id + " - about to get something from " + httpget.getURI());
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                    System.out.println(id + " - get executed");
                    Map map = objectMapper.readValue(response.getEntity().getContent(),Map.class);
                    Map data = (Map)((List)map.get("data")).get(0);
                    double ret =  Double.valueOf(data.get("last_price").toString());
                    price = ret;
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }

    }

}