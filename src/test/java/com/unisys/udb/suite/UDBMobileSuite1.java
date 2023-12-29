package com.unisys.udb.suite;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.unisys.udb.base.TestBase;
import com.unisys.udb.dataprovider.CsvDataProvider;
import com.unisys.udb.page.CookiesAndPrivacyPage;
import com.unisys.udb.page.RegistrationPage;
import com.unisys.udb.report.ReportGenerator;
import com.unisys.udb.testrailmanager.TestNGTestRailUploader;

import io.appium.java_client.android.AndroidDriver;

public class UDBMobileSuite1 extends TestBase {

	AndroidDriver driver = null;
	ReportGenerator reportGenerator = null;
	CookiesAndPrivacyPage cookiePage = null;
    RegistrationPage registrationPage=null;
	public UDBMobileSuite1() {
		super();
	}

	@BeforeSuite
	public void beforeSuite(ITestContext iTestContext) {
		this.setTestSuiteName("UDBMobileSuite1");
			this.launchApp(iTestContext);
	}

	@BeforeMethod
	public void beforeMethod(ITestContext iTestContext) {
		reportGenerator = new ReportGenerator();
		AndroidDriver driver = this.getListOfDrivers().get(this.getTestSuiteName());
		cookiePage = new CookiesAndPrivacyPage(driver, reportGenerator);
		registrationPage = new RegistrationPage(driver, reportGenerator);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		reportGenerator.endReport();
		//TestNGTestRailUploader.uploadTestResultsToTestRail(result);
	}

	@AfterSuite
	public void afterSuite(ITestContext iTestContext) {
		this.closeDriver(this.getListOfDrivers().get(this.getTestSuiteName()));
		this.stopAppiumServer();
	}

	@Test(priority = 1, groups = {
			"Level1" }, testName = "TestUDBRegistration", dataProvider = "csvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void testUDBRegistration(ITestContext iTestContext, HashMap<String, String> hashmap) {
		try {
			reportGenerator.setUpReportGenerator(hashmap.get("TestCaseNumber"), "testUDBRegistration");
			iTestContext.setAttribute("testDataMap", hashmap); // Test Data Map must be set for reporting purpose
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cookiePage.processTermsAndCookiesScreens(hashmap);
			registrationPage.performRegistrationProcess(hashmap);

		}catch (Exception ex) {
			ex.printStackTrace();
			reportGenerator.logMessage("Failed due to " + ex.getMessage(), Status.ERROR);
			//TestNGTestRailUploader.uploadTestResultsToTestRail("Failed", hashmap.get("TestCaseNumber"));
			Assert.fail("testUDBRegistration is failed due to " + ex.getMessage());
		}
		
	}

}
