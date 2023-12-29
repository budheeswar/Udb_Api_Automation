package com.unisys.udb.report;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.io.Files;
import com.unisys.udb.utils.ApplicationPropertyLoader;
import com.unisys.udb.utils.CommonUtils;

import io.appium.java_client.android.AndroidDriver;

public class ReportGenerator {

	private ExtentHtmlReporter htmlReporter = null;
	private ExtentReports extent = null;
	private ExtentTest test = null;
	private static final String BASE_REPORT_DIR = System.getProperty("user.dir");
	private String reportPath = null;
	
	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public ReportGenerator() {
		// Empty constructor to instantiate from test method
	}

	public void setUpReportGenerator(String testCaseNumber, String testMethodName) {
		reportPath = BASE_REPORT_DIR + File.separator +"test-output"+File.separator+"extentreports"+File.separator+testCaseNumber;
		//reportPath = BASE_REPORT_DIR + File.separator +testCaseNumber;
		File reportDirectory = new File(reportPath);
		try {
			if (!reportDirectory.exists()) {
				reportDirectory.mkdirs();
			}
		}catch(Exception ex) {
			Reporter.log("error while creating folder with testcase name");
		}
		String htmlFilePath = reportPath + File.separator + testMethodName + "_"
				+ CommonUtils.getFormatedDate("MMddyyyyhh_mm_ss") + ".html";
		htmlReporter = new ExtentHtmlReporter(htmlFilePath);
		System.out.println("html report path "+htmlReporter);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		htmlReporter.config().setDocumentTitle("UDB Mobile UI Testing Report"); // Title of Report
		htmlReporter.config().setReportName("UDB Mobile UI App Testing  Report"); // Name of the report
		htmlReporter.config().setTheme(Theme.STANDARD);// Default Theme of Report

		// General information related to application
		extent.setSystemInfo("Application Name", "UDB MOBILE UI AUTOMATION");
		extent.setSystemInfo("Author", "R Buddeeswar");
		test = extent.createTest(testMethodName);

	}

	public void logMessage(String message, Status logLevel) {
		Status level = Status.INFO;
		if (logLevel != null)
			level = logLevel;
		test.log(level, message);
		Reporter.log(message); // routing the message to testng reporter
	}

	public void logAndCaptureScreenshot(AndroidDriver driver, String methodName, String message, Status logLevel) {
		if (ApplicationPropertyLoader.getPropertyByName("isScreenshot").equalsIgnoreCase("true")) { 
			captureScreenShot(driver, methodName);
		}
		logMessage(message, logLevel);
	}

	private void captureScreenShot(AndroidDriver driver, String methodName) {
		System.out.println("Taking Screenshot of  " + methodName + " ");
		String destinationFileName = reportPath + File.separator + methodName + "-"
				+ CommonUtils.getFormatedDate("MMddYYYYhhmmss") + ".png";
		//String imgPath = System.getProperty("user.dir") + "/test-output/screenshots/" + methodName + ".jpg";
		if(driver!=null) {
			
			File source = driver.getScreenshotAs(OutputType.FILE);
			File reportDirectory = new File(reportPath);
			try {
				if (!reportDirectory.exists()) {
					reportDirectory.mkdirs();
				}
				File dest = new File(destinationFileName);
				Files.move(source, dest);
			} catch (IOException e) {
				System.out.println("Unable to Take Screenshot");
				e.printStackTrace();
			}
			System.out.println("Screenshot Captured Successfully");	
		}
		

	}

	public void endReport() {
		extent.flush();
	}

}
