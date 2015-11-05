import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
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

    private static String url ="https://etrade.gf.com.cn/entry";
    private static  String  session="JSESSIONID=030AA63B98607C0E34C2918EBF5F63F1; dse_sessionId=0EA34478BEE1723D05200854A91B3920; userId=*EB*88*B9*8B*1E*F0d*D4*E0Pa*08*D9*B5*A4*83G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00; name=value";
    private static String sessionId ="0EA34478BEE1723D05200854A91B3920";
/*
     private static String url ="https://trade.gf.com.cn/entry";
    private static  String  session="name=value; JSESSIONID=BE329CF69F2383C4FFC25F90B7B43252; dse_sessionId=A8545391B402DF2A9C2CB2AB5C712887; userId=*C4*A7*DD*F9*FE*89*92*F4*BEN*CE*EA*D8*AC*B3*00G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";
    private static String sessionId ="A8545391B402DF2A9C2CB2AB5C712887";*/
    public static void main(String[] args) throws Exception {
        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.

        long total =0l;
        for(int k =0;k<100;k++){


            long c1 = System.currentTimeMillis();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(100);
            CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm) .build();
            try {
                // create an array of URIs to perform GETs on
                String[] urisToGet = {
                        url+"?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878002&dse_sessionId="+sessionId,
                        url+"?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878003&dse_sessionId="+sessionId
                };
                // create a thread for each URI
                GetThread[] threads = new GetThread[urisToGet.length];
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
              //  System.out.println(threads[0].getPrice()+threads[1].getPrice());
               long times =  System.currentTimeMillis()-c1;
                total+=times;
               //System.out.println("end");


        } finally {
            httpclient.close();
        }

        }

        System.out.println(total);

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
                    String  responseBody = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);

                    System.out.println(responseBody);

                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }

    }

}