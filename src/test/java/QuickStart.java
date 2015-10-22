import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.JSONPObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by noside on 15-8-15.
 */
public class QuickStart {

    public static void main(String[] args) throws Exception {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
        CloseableHttpResponse response = null;
        try {
            String url = "http://infinitus-worldpay.ddns.net/orderapp/";
            String gfCookie = "TOKEN_EXPIRE_TIME=300;PHONE=85299999993;DISTRIBUTOR_ID=33333333;TOKEN=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJBUFBLRVkiLCJzdWIiOiJhY2Nlc3NfaW5maW5pdHVzX2VzYiIsImlhdCI6MTQ0MjQwNTgwMCwianRpIjoiWWdRcXVkVjJIX0J5ZXUzbW5OXzB3ZyIsInV0YSI6Int9In0.kQkn6SvPZqcMt7a05NBq16qO_OdOgz3U1t3xgDBETG6xWbFDtxTSf0wu4AwUDUNbddqvUvRup9cPM9p7Ex_-9_gB4RiHXQ6AOEQG49TsqvARU-Iipr_QW6m7H-QIU88fC04wpd548PQ8Tvmaq4Diuccfc7SJFHTu0PvTt9FlNkDQDQIVSJHZ2ut0l5S1rqjR9hOGOBjHZ2gQYtKXgZqaEwwfbhyRorcC-V1KKHUJ3HV1c5UCzPZ41szIItKuHLmEGAuQ_jY6dm-8RbM71QQPe3NB56osWw2sPHQpmzwj9ILdkXQUGGIk7hbLlfM7dsLOe18vd--CpcxbSBxYO9G8sg;";
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Cookie", gfCookie);
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httpPost.setEntity(entity);
            response = httpclient.execute(httpPost);
            String responseBody = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);
            System.out.print(response.getStatusLine().getStatusCode());
            System.out.print(responseBody);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


}
