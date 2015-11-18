package com.gt.bmf.service.impl;

import com.google.gson.Gson;
import com.gt.bmf.dao.BmfBaseDao;
import com.gt.bmf.dao.GfQueryLogDao;
import com.gt.bmf.pojo.GfQueryLog;
import com.gt.bmf.service.GfQueryLogService;
import com.gt.bmf.util.NumberUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//@Service("gfQueryLogBedService")
public class GfQueryLogBedServiceImpl extends BmfBaseServiceImpl<GfQueryLog> implements GfQueryLogService {
    @Value("${gf.cookie}")
    private String gfCookie;

    private static Logger logger = Logger.getLogger("GfQueryLogBedServiceImpl");

    @Value("${gf.session}")
    private String gfSession;
	private GfQueryLogDao gfQueryLogDao;

    //private String url ="https://trade.gf.com.cn/entry";
    private String url ="https://etrade.gf.com.cn/entry";

    private boolean lockBuyAction = false;
    private boolean lockSaleAction = false;

    private static List<Double> upSale = new ArrayList<Double>();
    private static List<Double> upBuy = new ArrayList<Double>();
    private static List<Double> downSale = new ArrayList<Double>();
    private static List<Double> downBuy = new ArrayList<Double>();
    DecimalFormat decimalFormat = new DecimalFormat("###.######");

    //@Autowired
	@Qualifier("gfQueryLogDao")
	@Override
	public void setBmfBaseDao(BmfBaseDao<GfQueryLog> bmfBaseDao) {
		this.bmfBaseDao = bmfBaseDao;
		this.gfQueryLogDao = (GfQueryLogDao) bmfBaseDao;
	}


    public void initVariance(double upSalePrice,double upBuyPrice,double downSalePrice,double downBuyPrice){
        //System.out.println("upBuyPrice["+upBuyPrice+"] upSalePrice["+upSalePrice+"] downBuyPrice["+downBuyPrice+"] downSalePrice["+downSalePrice+"]");
        upSale.add(upSalePrice);
        upBuy.add(upBuyPrice);
        downSale.add(downSalePrice);
        downBuy.add(downBuyPrice);
        if(upSale.size()>15){
            upSale.remove(0);
            upBuy.remove(0);
            downSale.remove(0);
            downBuy.remove(0);
        }
    }

    public void checkData() throws IOException, InterruptedException {
        //long c1 = System.currentTimeMillis();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(10);
        CloseableHttpClient  httpclient = HttpClients.custom().setConnectionManager(cm) .build();
        try {
            // create an array of URIs to perform GETs on a
            String[] urisToGet = {
                    "https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878004&dse_sessionId="+gfSession,
                    "https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878005&dse_sessionId="+gfSession
            };
            GetThread[] threads = new GetThread[2];
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
            double upSalePrice  =  Double.parseDouble(upData.get("sale_price1").toString());
            double downSalePrice  =  Double.parseDouble(downData.get("sale_price1").toString());
            double upBuyPrice  =  Double.parseDouble(upData.get("buy_price1").toString());
            double downBuyPrice  =  Double.parseDouble(downData.get("buy_price1").toString());

            initVariance(upSalePrice,upBuyPrice,downSalePrice,downBuyPrice);
            double buyTotal = upBuyPrice+downBuyPrice;
            double saleTotal = upSalePrice+downSalePrice;
            if(saleTotal<1.998d){
                this.buy(upSalePrice,downSalePrice);
                double upSaleAmount  =  Integer.valueOf(upData.get("sale_amount1").toString());
                double downSaleAmount  =  Integer.valueOf(downData.get("sale_amount1").toString());
                GfQueryLog model = new GfQueryLog();
                model.setType("B");
                model.setDownPrice(downSalePrice);
                model.setUpPrice(upSalePrice);
               // model.setLastPrice(saleTotal);
                model.setLogTime(new Date());

                double v1 = NumberUtils.getStandardDiviation(upSale);
                double v2 = NumberUtils.getStandardDiviation(downSale);

                model.setUpVariance(v1);
                model.setDownVariance(v2);

                this.save(model);

               System.out.println("Buy：S+["+String.valueOf(saleTotal)+"]　  code[878004,878005]");
               System.out.println("upData->"+upData);
               System.out.println("downData->"+downData);

               System.out.println("-------------------------------------------------");
            }else if(buyTotal>2.003d){
                this.sale(upBuyPrice, downBuyPrice);

                GfQueryLog model = new GfQueryLog();
                model.setType("S");
                model.setDownPrice(upBuyPrice);
                model.setUpPrice(downBuyPrice);

                //model.setLastPrice(buyTotal);
                model.setLogTime(new Date());

                double v1 = NumberUtils.getStandardDiviation(upBuy);
                double v2 = NumberUtils.getStandardDiviation(downBuy);

                model.setUpVariance(v1);
                model.setDownVariance(v2);

                this.save(model);

             //  System.out.println((System.currentTimeMillis()-c1) +"ms, upPrice["+upBuyPrice+"] download["+downBuyPrice+"] sale price["+buyTotal+"]" );
               System.out.println("Sale：S+["+String.valueOf(saleTotal)+"]　B+["+String.valueOf(buyTotal)+"] code[878004,878005]");
               System.out.println("upData->"+upData);
               System.out.println("downData->"+downData);
               System.out.println("-------------------------------------------------");

            }else{
               /* double v1 = NumberUtils.getStandardDiviation(upBuy);
                System.out.println( "---->标准差["+decimalFormat.format(v1)+"]" );*/
            }

            //System.out.println("end");
        } finally {
            httpclient.close();
        }

    }
    public void buy(double upPrice,double downPrice){
       // long c = System.currentTimeMillis();
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("Cookie",gfCookie);
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
                formparams.add(new BasicNameValuePair("method", "nxbdoubleentrust"));
                formparams.add(new BasicNameValuePair("dse_sessionId", gfSession));

                formparams.add(new BasicNameValuePair("fund_code", "878004"));
                formparams.add(new BasicNameValuePair("entrust_amount", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price", String.valueOf(upPrice)));

                formparams.add(new BasicNameValuePair("fund_code_1", "878005"));
                formparams.add(new BasicNameValuePair("entrust_amount_1", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price_1",String.valueOf(downPrice)));
                formparams.add(new BasicNameValuePair("entrust_bs", "1"));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);


                httpPost.setEntity(entity);

                response = httpclient.execute(httpPost);
                //String responseBody = EntityUtils.toString(entity);

               String  responseBody = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);

               System.out.println("878004["+upPrice+"] 878005["+downPrice+"]");
               System.out.println(responseBody);

               // System.out.println((System.currentTimeMillis() - c) + " ");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
               // lockBuyAction = true;
            }
    }

    /**
     * classname:com.gf.etrade.control.NXBUF2Control
     method:nxbdoubleentrust
     dse_sessionId:668E52FA4EF2BA7ECB9B92DC90B1BE62
     fund_code:878004
     entrust_amount:1000
     entrust_price:0.625
     fund_code_1:878005
     entrust_amount_1:1000
     entrust_price_1:1.37
     entrust_bs:2
     * */
    public void sale(double upPrice,double downPrice){
  /*      if(lockSaleAction){
           System.out.println("lockSaleAction is true,please unlock");
            return;
        }else{*/

            CloseableHttpClient httpclient = HttpClients.createDefault();

            CloseableHttpResponse response = null;
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("Cookie",gfCookie);
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("classname", "com.gf.etrade.control.NXBUF2Control"));
                formparams.add(new BasicNameValuePair("method", "nxbdoubleentrust"));
                formparams.add(new BasicNameValuePair("dse_sessionId", gfSession));

                formparams.add(new BasicNameValuePair("fund_code", "878004"));
                formparams.add(new BasicNameValuePair("entrust_amount", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price", String.valueOf(upPrice)));

                formparams.add(new BasicNameValuePair("fund_code_1", "878005"));
                formparams.add(new BasicNameValuePair("entrust_amount_1", "1000"));
                formparams.add(new BasicNameValuePair("entrust_price_1",String.valueOf(downPrice)));
                formparams.add(new BasicNameValuePair("entrust_bs", "2"));
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                httpPost.setEntity(entity);
                response = httpclient.execute(httpPost);
                String  responseBody = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);


               System.out.println("878004["+upPrice+"] 878005["+downPrice+"]");
               System.out.println(responseBody);

                EntityUtils.consume(entity);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

               // lockSaleAction = true;
            }
     //   }
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
        Gson gson = new Gson();

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
               //System.out.println(id + " - about to get something from " + httpget.getURI());
                CloseableHttpResponse response = httpClient.execute(httpget, context);
                try {
                   //System.out.println(id + " - get executed");
                /*    Map map = objectMapper.readValue(response.getEntity().getContent(),Map.class);
                    Map data = (Map)((List)map.get("data")).get(0);*/
                    Map map = gson.fromJson(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8), Map.class);
                    Map data = (Map) ((List) map.get("data")).get(0);
                    this.data = data;
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
