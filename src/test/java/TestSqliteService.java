import com.gt.bmf.service.GfQueryLogService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestSqliteService {

    private static GfQueryLogService service;


    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/bmf_applicationContext.xml");
        service = (GfQueryLogService) ctx.getBean("gfQueryLogBedService");
        for(int i=0;i<25;i++)
           service.buy(1d,1d);

        System.exit(0);

	}

    public void testCreateDB(){


    }

}
