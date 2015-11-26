package com.gt.bmf.service.impl;

import com.gt.bmf.dao.GfQueryLogDao;
import com.gt.bmf.dao.BmfBaseDao;
import com.gt.bmf.pojo.GfQueryLog;
import com.gt.bmf.service.GfQueryLogService;
import com.gt.bmf.util.NumberUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("gfQueryLogService")
public class GfQueryLogServiceImpl extends BmfBaseServiceImpl<GfQueryLog> implements GfQueryLogService {
    @Value("${gf.cookie}")
    private String gfCookie;
    @Value("${gf.session}")
    private String gfSession;
	private GfQueryLogDao gfQueryLogDao;

    private String url ="https://trade.gf.com.cn/entry";

    private boolean lockBuyAction = false;
    private boolean lockSaleAction = false;



   // @Autowired
	@Qualifier("gfQueryLogDao")
	@Override
	public void setBmfBaseDao(BmfBaseDao<GfQueryLog> bmfBaseDao) {
		this.bmfBaseDao = bmfBaseDao;
		this.gfQueryLogDao = (GfQueryLogDao) bmfBaseDao;

	}

    @PostConstruct
    public void init(){

    }

    public void checkData() throws IOException, InterruptedException {
        //long c1 = System.currentTimeMillis();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(10);
        CloseableHttpClient  httpclient = HttpClients.custom().setConnectionManager(cm) .build();
        try {
            // create an array of URIs to perform GETs on
            String[] urisToGet = {
                    "https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878002&dse_sessionId="+gfSession,
                    "https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878003&dse_sessionId="+gfSession
            };
            // create a thread for each URI
            GetThread[] threads = new GetThread[urisToGet.length];
          //  String session="Hm_lvt_07e1b3469e412552a15451441d5e3973=1438569747,1439519153; name=value; JSESSIONID=C40E4F8E71E716B866BCC9E95F6FB993; dse_sessionId=DCCF5D64389FC474A8B841A3EFDA9109; userId=*A0*10*B1t*0F*E2*0E*B1*91z*C66*C2*BCa*AEG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";
           // String session="Hm_lvt_07e1b3469e412552a15451441d5e3973=1438569747,1439519153; name=value; JSESSIONID=C40E4F8E71E716B866BCC9E95F6FB993; dse_sessionId=DCCF5D64389FC474A8B841A3EFDA9109; userId=*A0*10*B1t*0F*E2*0E*B1*91z*C66*C2*BCa*AEG*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8G*97*883*91G*16bw*22*A05*A8*CCL8*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00*00";
            for (int i = 0; i < threads.length; i++) {
                HttpGet httpget = new HttpGet(urisToGet[i]);
                httpget.addHeader("Cookie",gfCookie);
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



            Map upData  = threads[0].getData();
            Map downData  = threads[1].getData();
            double upSalePrice  =  Double.valueOf(upData.get("sale_price1").toString());
            double downSalePrice  =  Double.valueOf(downData.get("sale_price1").toString());

            double upBuyPrice  =  Double.valueOf(upData.get("buy_price1").toString());
            double downBuyPrice  =  Double.valueOf(downData.get("buy_price1").toString());

            double upLastPrice  =  Double.valueOf(upData.get("last_price").toString());
            double downloadLastPrice  =  Double.valueOf(downData.get("last_price").toString());



            double saleTotal = upSalePrice+downSalePrice;
            double buyTotal = upBuyPrice+downBuyPrice;

            double lastTotal = upLastPrice+downloadLastPrice;


            if(saleTotal<1.998d){
                this.buy(upSalePrice,downSalePrice);
                GfQueryLog model = new GfQueryLog();
                model.setType("B");
                model.setDownPrice(downSalePrice);
                model.setUpPrice(upSalePrice);
                //model.setQuality(2000);
             //   model.setLastPrice(saleTotal);
                model.setLogTime(new Date());

            /*    double margin = (2-(total * 3/10000 + total))*2000;
                model.setMargin(margin);*/
                this.save(model);
                System.out.println("TO：S+["+ NumberUtils.format(saleTotal)+"]　B+["+NumberUtils.format(buyTotal)+"] Last["+NumberUtils.format(lastTotal)+"] code[878002,878003]");
                System.out.println("-------------------------------------------------");
                //System.out.println((System.currentTimeMillis()-c1) +"ms, upPrice["+upBuyPrice+"] download["+downBuyPrice+"] buy price["+saleTotal+"]" );
            }else if(buyTotal>2.003d){
                this.sale(upBuyPrice,downBuyPrice);
                GfQueryLog model = new GfQueryLog();
                model.setType("S");
                model.setDownPrice(upBuyPrice);
                model.setUpPrice(downBuyPrice);
               // model.setQuality(2000);
            //    model.setLastPrice(buyTotal);
                model.setLogTime(new Date());
        /*        double margin = (total-2)*2000 - ((total-2)*2000*1/10000);
                model.setMargin(margin);*/
                this.save(model);

                System.out.println("TO：S+["+ NumberUtils.format(saleTotal)+"]　B+["+NumberUtils.format(buyTotal)+"] Last["+NumberUtils.format(lastTotal)+"] code[878002,878003]");
                System.out.println("-------------------------------------------------");

             //   System.out.println((System.currentTimeMillis()-c1) +"ms, upPrice["+upBuyPrice+"] download["+downBuyPrice+"] sale price["+buyTotal+"]" );
            }
           // System.out.println("end");


            //  System.out.println("UP：S1["+upSalePrice+"]　B1["+upBuyPrice+"] Last["+upLastPrice+"]");
            //  System.out.println("DW：S1["+downSalePrice+"]　B1["+downBuyPrice+"] Last["+downloadLastPrice+"]");



        } finally {
            httpclient.close();
        }

    }

    public void buy(double upPrice,double downPrice){
        if(lockBuyAction){
            System.out.println("lockBuyAction is true,please unlock");
            return;
        }else{

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(100);
            CloseableHttpClient  httpclient = HttpClients.custom().setConnectionManager(cm) .build();
            CloseableHttpResponse response = null;
            try {

                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("Cookie",gfCookie);
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
                formparams.add(new BasicNameValuePair("method", "nxbdoubleentrust"));
                formparams.add(new BasicNameValuePair("dse_sessionId", gfSession));

                formparams.add(new BasicNameValuePair("fund_code", "878002"));
                formparams.add(new BasicNameValuePair("entrust_amount", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price", (String.valueOf(upPrice))));

                formparams.add(new BasicNameValuePair("fund_code_1", "878003"));
                formparams.add(new BasicNameValuePair("entrust_amount_1", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price_1",String.valueOf(downPrice)));
                formparams.add(new BasicNameValuePair("entrust_bs", "1"));


                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

                System.out.println("formparams->"+entity.toString());


                httpPost.setEntity(entity);

                response = httpclient.execute(httpPost);

                String  responseBody = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);

                System.out.print(response.getStatusLine().getStatusCode());
                System.out.print(responseBody);



            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lockBuyAction = true;
            }
        }
    }

    /**
     * classname:com.gf.etrade.control.NXBUF2Control
     method:nxbdoubleentrust
     dse_sessionId:668E52FA4EF2BA7ECB9B92DC90B1BE62
     fund_code:878002
     entrust_amount:1000
     entrust_price:0.625
     fund_code_1:878003
     entrust_amount_1:1000
     entrust_price_1:1.37
     entrust_bs:2
     * */
    public void sale(double upPrice,double downPrice){
        if(lockSaleAction){
            System.out.println("lockSaleAction is true,please unlock");
            return;
        }else{

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(100);
            CloseableHttpClient  httpclient = HttpClients.custom().setConnectionManager(cm) .build();
            CloseableHttpResponse response = null;
            try {

                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("Cookie",gfCookie);
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
                formparams.add(new BasicNameValuePair("method", "nxbdoubleentrust"));
                formparams.add(new BasicNameValuePair("dse_sessionId", gfSession));

                formparams.add(new BasicNameValuePair("fund_code", "878002"));
                formparams.add(new BasicNameValuePair("entrust_amount", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price", String.valueOf(upPrice)));

                formparams.add(new BasicNameValuePair("fund_code_1", "878003"));
                formparams.add(new BasicNameValuePair("entrust_amount_1", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price_1",String.valueOf(downPrice)));
                formparams.add(new BasicNameValuePair("entrust_bs", "2"));


                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

                httpPost.setEntity(entity);


                System.out.println("formparams->"+entity.toString());

                response = httpclient.execute(httpPost);

                String  responseBody = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);

                System.out.print(response.getStatusLine().getStatusCode());
                System.out.print(responseBody);



            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lockSaleAction = true;
            }
        }
    }




    //当日委托
    /**
     * classname:com.gf.etrade.control.NXBUF2Control
     method:nxbQueryEntrust
     query_type:1
     query_mode:1
     prodta_no:98
     entrust_no:0
     prod_code:
     start_date:0
     end_date:0
     position_str:0
     limit:50
     dse_sessionId:668E52FA4EF2BA7ECB9B92DC90B1BE62
     fund_code:
     start:0
     * */
    public void paddingRecord(){

    }

    static class GetThread extends Thread {
        //ObjectMapper objectMapper = new ObjectMapper();

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;
       // private double price;
        private Map data;

        public Map getData() {
            return data;
        }

   /*     public double getPrice(){
            return  price;
        }
*/
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
               // System.out.println(id + " - about to get something from " + httpget.getURI());
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                   // System.out.println(id + " - get executed");
                   // Map map = objectMapper.readValue(response.getEntity().getContent(),Map.class);
                  //  Map data = (Map)((List)map.get("data")).get(0);
                 //   this.data = data;

                    /*double ret =  Double.valueOf(data.get("last_price").toString());
                    price = ret;*/
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }

    }



    public void setLockBuyAction(boolean lockBuyAction) {
        this.lockBuyAction = lockBuyAction;
    }

    public boolean isLockBuyAction() {
        return lockBuyAction;
    }

    public boolean isLockSaleAction() {
        return lockSaleAction;
    }

    public void setLockSaleAction(boolean lockSaleAction) {
        this.lockSaleAction = lockSaleAction;
    }

}
