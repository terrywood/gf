package com.gt.bmf.service.impl;

import com.google.gson.Gson;
import com.gt.bmf.dao.BmfBaseDao;
import com.gt.bmf.dao.GfQueryLogDao;
import com.gt.bmf.dao.GridTradingDao;
import com.gt.bmf.pojo.GfQueryLog;
import com.gt.bmf.pojo.GridTrading;
import com.gt.bmf.service.GridTradingService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2015/11/11 0011.
 */
@Service("gridTradingService")
public class GridTradingServiceImpl  implements GridTradingService {
    @Value("${gf.cookie}")
    private String gfCookie;
    @Value("${gf.session}")
    private String gfSession;
    @Autowired
    private GridTradingDao gridTradingDao;


    CloseableHttpClient httpClient ;


    @PostConstruct
    public  void init(){
/*
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
*/
    }
/*
    public void check(){
        check2();check1();
        System.out.println("------------------------");
    }
    public void check2(){
        long c = System.currentTimeMillis();
       /*//* Gson gson = new Gson();
        httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://trade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878002&dse_sessionId="+gfSession);
        httpget.addHeader("Cookie",gfCookie);
        String result = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpget);
            result = IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8);
          *//*  Map map = gson.fromJson(IOUtils.toString(response.getEntity().getContent(), Consts.UTF_8), Map.class);
            Map data = (Map) ((List) map.get("data")).get(0);
            System.out.println(data);*//*
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("cli:"+(System.currentTimeMillis() - c));
    }*/



    public  void check() {
       long c = System.currentTimeMillis();
       Gson gson = new Gson();
      // String result = null;
       String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878003&dse_sessionId="+gfSession;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie",  gfCookie);
            connection.connect();
           // result= IOUtils.toString(connection.getInputStream(), Consts.UTF_8);
            Map map = gson.fromJson(IOUtils.toString(connection.getInputStream(), Consts.UTF_8), Map.class);
            Map data = (Map)((List) map.get("data")).get(0);
            Double lastPrice = MapUtils.getDouble(data,"last_price");
            System.out.println(lastPrice);

            GridTrading model = new GridTrading();
            model.setFund("878002");
            model.setPrice(lastPrice);
            model.setLogTime(new Date());
            gridTradingDao.save(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("con:"+(System.currentTimeMillis() - c));
    }


}
