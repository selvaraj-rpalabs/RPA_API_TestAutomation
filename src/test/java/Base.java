import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class Base {


    public static ExtentReports extent = new ExtentReports();
    public static ExtentSparkReporter spark=new ExtentSparkReporter("./reports/Spark.html");


    @BeforeSuite
    public void setUp() throws  Exception{
        spark.loadXMLConfig("./html-config.xml");
    }


}


