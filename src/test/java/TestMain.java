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

        String[] aa = StringUtils.split("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z", " ");
        for(String a : aa){
            System.out.println(a);
           try {
                FileUtils.copyURLToFile(new URL("http://cn.gtomato.com:10080/lkk/bochk/xml/10.jsp?prefix=A" + a), new File("d:/A" + a + ".xml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
