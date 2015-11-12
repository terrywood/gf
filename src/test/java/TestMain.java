import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Administrator on 2015/11/11 0011.
 */
public class TestMain {
    public static void main(String[] args)  {

  /*      String[] aa = StringUtils.split("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z", " ");
        for(String a : aa){
            System.out.println(a);
           try {
                FileUtils.copyURLToFile(new URL("http://cn.gtomato.com:10080/lkk/bochk/xml/10.jsp?prefix=A" + a), new File("d:/A" + a + ".xml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/


        TestMain test = new TestMain();

        test.checkPrice(100d);
        test.checkPrice(99d);
        test.checkPrice(98d);
        test.checkPrice(97d);
        test.checkPrice(96d);
        test.checkPrice(95d);
        test.checkPrice(94.5d);


    }

    double intPrice = 100d;
    double grid = intPrice*0.01;
    int lastNet =0;
    //int maxNet = 5;
    int minNet = -5;


    public void  checkPrice(double lastPrice){
       /* double upPrice = grid*(lastNet+1) +intPrice;
        double downPrice = grid*(lastNet-1) +intPrice;*/
        double curPrice = grid*(lastNet) +intPrice;
        int step = new Double((lastPrice - curPrice)/grid).intValue();
        if(step>0){
            lastNet+=step;
            System.out.println("step["+step+"] lastNet["+lastNet+"]");
            System.out.println("sell:"+lastPrice);

        /*    if(lastNet>maxNet){
                System.out.println("out of max net");
            }else{
                System.out.println("sell:"+lastPrice);
            }*/
        }else if(step<0){
            lastNet+=step;
            if(lastNet<minNet){
                System.out.println("out of min net");
            }else{
                System.out.println("step["+step+"] lastNet["+lastNet+"]");
                System.out.println("buy");
            }
        }else{
            System.out.println("noting");
        }

     //   System.out.println("upPrice["+upPrice+"] downPrice["+downPrice+"] lastNet["+lastNet+"]");
    }
}
