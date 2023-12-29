package com.unisys.udb.base;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.Reporter;

import com.unisys.udb.constants.UDBMobileConstants;
import com.unisys.udb.utils.ApplicationPropertyLoader;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class TestBase {

	protected AndroidDriver driver = null;
	protected String testSuiteName = null;
	protected String sTestCaseNumber = null;
	protected String sTestCaseName = null;
	protected String sReportDirectory = null;

	private static DesiredCapabilities dc;
	private static AppiumDriverLocalService server;

	ThreadLocal<AndroidDriver> localDriver = new ThreadLocal<>();

	protected HashMap<String, AndroidDriver> driversMap = new HashMap<>();

	public Map<String, AndroidDriver> getListOfDrivers() {
		return driversMap;
	}

	public void setListOfDrivers(Map<String, AndroidDriver> driversMap) {
		this.driversMap = (HashMap<String, AndroidDriver>) driversMap;
	}

	public AndroidDriver getLocalDriver() {
		return localDriver.get();
	}

	public void setLocalDriver(AndroidDriver androidDriver) {
		this.localDriver.set(androidDriver);
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}

	public String getsReportDirectory() {
		return sReportDirectory;
	}

	public void setsReportDirectory() {
		if (this.getsTestCaseNumber() != null) {
			this.sReportDirectory = System.getProperty(UDBMobileConstants.USER_DIR) + File.separator
					+ ApplicationPropertyLoader.getPropertyByName("reportFolder") + File.separator
					+ this.getsTestCaseNumber();
		} else {
			this.sReportDirectory = System.getProperty(UDBMobileConstants.USER_DIR) + File.separator
					+ ApplicationPropertyLoader.getPropertyByName("reportFolder");
		}
	}

	public String getsTestCaseNumber() {
		return this.sTestCaseNumber;
	}

	public void setsTestCaseNumber(String sTestCaseNumber) {
		this.sTestCaseNumber = sTestCaseNumber;
	}

	public String getsTestCaseName() {
		return this.sTestCaseName;
	}

	public void setsTestCaseName(String sTestCaseName) {
		this.sTestCaseName = sTestCaseName;
	}

	public void startAppiumServer() {
		System.out.println(ApplicationPropertyLoader.getPropertyByName("main.js"));
		try {
			if (server == null) {
				server = new AppiumServiceBuilder()
						.withAppiumJS(new File(ApplicationPropertyLoader.getPropertyByName("main.js")))
						.withIPAddress(ApplicationPropertyLoader.getPropertyByName("ipAddress"))
						.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
						.withLogFile(
								new File(System.getProperty("user.dir") + "/test-output/appiumlogs/AppiumLogsFile.log"))
						.withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/").usingPort(4723)
						.withTimeout(Duration.ofSeconds(300)).build();
				server.start();
			} else {
				server.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void stopAppiumServer() {
		if (server != null) {
			server.stop();
		}
	}

	public void launchApp(ITestContext iTestContext) {
		this.startAppiumServer();
		try {
			// Set Desired Capabilities
			dc = this.setUpDesiredCapabilities();
			URL url = new URL(ApplicationPropertyLoader.getPropertyByName("appium.server.url"));
			driver = new AndroidDriver(url, dc);

			this.setLocalDriver(driver);
			driversMap.put(testSuiteName, this.getLocalDriver());
			driver = this.getLocalDriver();
		} catch (Exception e) {
			Reporter.log("Exception occured while launching APP");
			e.printStackTrace();
		}

		this.manageDriver(iTestContext);
	}

	private DesiredCapabilities setUpDesiredCapabilities() {
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability("automationName", ApplicationPropertyLoader.getPropertyByName("automationName"));
		dc.setCapability("platformName", ApplicationPropertyLoader.getPropertyByName("platformName"));
		dc.setCapability("platformVersion", ApplicationPropertyLoader.getPropertyByName("platformVersion").toString());
		dc.setCapability("deviceName", ApplicationPropertyLoader.getPropertyByName("deviceName"));
//      dc.setCapability("app", "C:\\Users\\BuddeesR\\Downloads\\udbMobile_V0.0.1.apk");

		dc.setCapability("appPackage", ApplicationPropertyLoader.getPropertyByName("appPackage"));
		dc.setCapability("appActivity", ApplicationPropertyLoader.getPropertyByName("appActivity"));
		return dc;
	}

	@SuppressWarnings("unchecked")
	private void manageDriver(ITestContext iTestContext) {
		// store and manage the driver list at class level
		String invokingClsName = iTestContext.getClass().getName();
		List<AndroidDriver> androidDriverList = null;
		if (iTestContext.getAttribute(invokingClsName) != null) {
			androidDriverList = (ArrayList<AndroidDriver>) iTestContext.getAttribute(invokingClsName);
		} else {
			androidDriverList = new ArrayList<>();
		}
		androidDriverList.add(driver);
		iTestContext.setAttribute(invokingClsName, androidDriverList);
	}

	public void closeDriver(AndroidDriver androidDriver) {
		try {
			androidDriver.quit();
		} catch (Exception ex) {
			// Ignore if there is an exception. no need to take any action here
		}
	}

	public AndroidDriver getDriver() {
		return this.driver;
	}

}
