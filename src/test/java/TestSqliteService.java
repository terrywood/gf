import com.google.gson.Gson;
import com.gt.bmf.service.GfQueryLogService;
import com.gt.bmf.service.GridTradingService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class TestSqliteService {

    private static GridTradingService service;


    public static void main(String[] args) throws Exception {
/*
        String log4jConfPath = "D:\\Documents\\gf\\src\\main\\java\\log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);*/
      //  BasicConfigurator.configure();


     /*   System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
*/
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/bmf_applicationContext.xml");

        service = (GridTradingService) ctx.getBean("gridTradingService");

/*

        Gson gson = new Gson();
        List<ArrayList> list  =gson.fromJson(new FileReader("D:\\Documents\\gf\\doc\\878004\\20151113.json"),List.class);
        for(ArrayList al :list){
            Double price  = (Double)al.get(1);
            service.checkPrice(price);
        }*/


      /*  for(int i=0;i<25;i++)
           service.buy(1d,1d);

        System.exit(0);*/

        //service.save();

	}

    public void testCreateDB(){


    }

}
