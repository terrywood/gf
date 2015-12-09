package com.gt.bmf.service.impl;

import com.google.gson.Gson;
import com.gt.bmf.dao.GridTradingDao;
import com.gt.bmf.pojo.GridTrading;
import com.gt.bmf.service.GridTradingService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/11 0011.
 */
@Service("gridTrading300Service")
public class GridTrading300ServiceImpl implements GridTradingService {
    @Value("${gf.cookie}")
    private String gfCookie;
    @Value("${gf.session}")
    private String gfSession;
    @Autowired
    private GridTradingDao gridTradingDao;

    private double intPrice;
    private double grid = intPrice*0.007;
    private int lastNet =0;
    private int minNet = -10;
    private int volume =1000;
    //int maxNet = 5;

    @PostConstruct
    public void init(){
        System.out.println("init method............");
        intPrice = getLastPrice();
        grid = intPrice*0.01;
        System.out.println(intPrice);
        System.out.println(grid);
    }

    public void setInitPrice(double intPrice){
        this.intPrice = intPrice;
        grid = intPrice*0.01;
        System.out.println(intPrice);
        System.out.println(grid);
    }



    public  double getLastPrice(){
        Gson gson = new Gson();
        String httpUrl ="https://etrade.gf.com.cn/entry?classname=com.gf.etrade.control.NXBUF2Control&method=nxbQueryPrice&fund_code=878002&dse_sessionId="+gfSession;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie",  gfCookie);
            connection.connect();
            Map map = gson.fromJson(IOUtils.toString(connection.getInputStream(), Consts.UTF_8), Map.class);
            Map data = (Map)((List) map.get("data")).get(0);
            return MapUtils.getDouble(data,"last_price");
          //
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void check() {
        checkPrice(getLastPrice());
    }
    public void  order(double lastPrice, int amount, String bs){
        //System.out.println("amount order"+amount);
        String httpUrl ="https://etrade.gf.com.cn/entry?entrust_bs="+bs+"&auto_deal=true&classname=com.gf.etrade.control.NXBUF2Control&method=nxbentrust&fund_code=878002&dse_sessionId="+gfSession+"&entrust_price="+lastPrice+"&entrust_amount="+amount;
        try {

           URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url .openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie",  gfCookie);
            connection.connect();
            String result = IOUtils.toString(connection.getInputStream(), Consts.UTF_8);
            System.out.println(result);
            GridTrading model = new GridTrading();
            model.setFund("878002");
            model.setPrice(lastPrice);
            model.setLogTime(new Date());
            model.setType(bs);
            model.setAmount(amount);
            model.setLastNet(lastNet);
            gridTradingDao.save(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  checkPrice(double lastPrice){
       /* double upPrice = grid*(lastNet+1) +intPrice;
        double downPrice = grid*(lastNet-1) +intPrice;*/
        double curPrice = grid*(lastNet) +intPrice;
        int step = new Double((lastPrice - curPrice)/grid).intValue();
        //System.out.println("lastPrice["+lastPrice+"] gridPrice["+curPrice+"] step["+step+"]");
        if(step>0){
            lastNet+=step;
            if(lastNet>minNet){
                order(lastPrice,Math.abs(volume*step),"2");//sell
            }
        }else if(step<0){
            lastNet+=step;
            if(lastNet>=minNet){
                order(lastPrice,Math.abs(volume*step),"1"); //buy
            }
        }
    }
}
