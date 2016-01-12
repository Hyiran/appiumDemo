package main.java.com.dbyl.appiumCore.tests;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import main.java.com.dbyl.appiumServer.AppiumServerUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @since 2015-6
 * @author Young
 */
public class AppDemo
{
    private AndroidDriver<MobileElement> driver;

    @BeforeClass
    public void startAppiumServer() throws IOException, InterruptedException
    {
        AppiumServerUtils.startServer();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception
    {
        // set up appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "5.1");
        // if no need install don't add this

        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "apps");
        File app = new File(appDir, "apppiumDemo.apk");
        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());

        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, "cn.dbyl.appiumdemo");
//        // support Chinese
//        capabilities.setCapability("unicodeKeyboard", "True");
//        capabilities.setCapability("resetKeyboard", "True");
        // no need sign
        capabilities.setCapability("noSign", "True");
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, ".MainActivity");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

    }

    @Test(groups = { "appiumDemo" })
    public void swipeTest()
    {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        MobileElement textView = driver.findElementByAccessibilityId("for appium test");
        Assert.assertEquals(textView.getText(), "appiumDemo");
        MobileElement button = driver
                .findElementByAndroidUIAutomator("new UiSelector().className(\"android.widget.Button\")");
        Assert.assertEquals(driver.findElementByClassName("android.widget.Button").getText(), "button");
        button.click();
        textView = driver.findElementById("cn.dbyl.appiumdemo:id/text1");
        Assert.assertEquals(textView.getText(), "You just click the button");

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception
    {
        driver.quit();
        AppiumServerUtils.stopAppiumServer("4723");
    }

}