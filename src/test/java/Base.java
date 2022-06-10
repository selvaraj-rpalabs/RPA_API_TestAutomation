import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Base {


    public static ExtentReports extent = new ExtentReports();
    public static ExtentSparkReporter spark=new ExtentSparkReporter("./reports/Spark.html");


    @BeforeSuite
    public void setUp() throws  Exception{
        spark.loadXMLConfig("./html-config.xml");
        extent.attachReporter(spark);
    }

    @AfterSuite
    public void tearDown() {
        extent.flush();
    }


}


