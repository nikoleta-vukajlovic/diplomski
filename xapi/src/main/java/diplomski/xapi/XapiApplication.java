package diplomski.xapi;

import com.rusticisoftware.tincan.Activity;
import com.rusticisoftware.tincan.Agent;
import com.rusticisoftware.tincan.RemoteLRS;
import com.rusticisoftware.tincan.Statement;
import com.rusticisoftware.tincan.TCAPIVersion;
import com.rusticisoftware.tincan.Verb;
import diplomski.xapi.services.Service;
import java.net.MalformedURLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"diplomski.xapi"})
public class XapiApplication {

    public static void main(String[] args) throws MalformedURLException, Exception {
        SpringApplication.run(XapiApplication.class, args);

    }

}
