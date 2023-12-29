package com.unisys.udb.page;

import static org.testng.Assert.assertEquals;

import java.awt.Point;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.unisys.udb.report.ReportGenerator;
import com.unisys.udb.utils.SeleniumUtils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.*;

public class RegistrationPage {

	private AndroidDriver driver = null;
	private ReportGenerator reportGenertor = null;

	@FindBy(xpath = "//android.widget.TextView[1]")
	WebElement welcomeTextElement;

	@FindBy(xpath = "//android.widget.Button[@content-desc=\"Register for Mobile Banking\"]")
	WebElement registerForMobilebankingBtn;

	@FindBy(xpath = "//android.widget.EditText")
	WebElement accountNumberInputBox;

	@FindBy(xpath = "//android.widget.Button[@content-desc=\"Next\"]/android.widget.TextView")
	WebElement nextButton;

	@FindBy(xpath = "//android.widget.EditText")
	WebElement dateOfBirthInputBox;

	@FindBy(xpath = "//android.widget.NumberPicker[2]/android.widget.EditText")
	WebElement dayElement;

	@FindBy(xpath = "//android.widget.NumberPicker[2]/android.widget.Button[1]")
	WebElement dayElementDestination;

	public RegistrationPage(AndroidDriver driver, ReportGenerator reportGenerator) {
		this.reportGenertor = reportGenerator;
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void performRegistrationProcess(HashMap<String, String> hashmap) throws InterruptedException {

		Assert.assertTrue(welcomeTextElement.isDisplayed());
		reportGenertor.logMessage("User in WElcome Screen to perform Registration", Status.PASS);
		
		SeleniumUtils.doClick(driver, registerForMobilebankingBtn);
		Thread.sleep(2000);
		driver.pressKey(new KeyEvent(AndroidKey.BACK));
		Thread.sleep(2000);
		SeleniumUtils.doClick(driver, registerForMobilebankingBtn);
		Thread.sleep(2000);
		SeleniumUtils.doClick(driver, accountNumberInputBox);
		
		Thread.sleep(1000);
		// Verifying Account Number and Date of Birth Mismatch
		/*
		 * SeleniumUtils.sendKeys(driver, accountNumberInputBox, "123456789098");
		 * 
		 * reportGenertor.logAndCaptureScreenshot(driver, "AccountNumberEntered",
		 * "Account Number Entered and Screenshot Taken", Status.PASS);
		 * Thread.sleep(1000); SeleniumUtils.doClick(driver, nextButton);
		 * Thread.sleep(1000); dateOfBirthInputBox.click(); Thread.sleep(1000);
		 * 
		 * driver.findElement(By.id("android:id/button1")).click();
		 * 
		 * SeleniumUtils.doClick(driver, nextButton);
		 * 
		 * 
		 * WebElement AccAndDobMisMatchElement = driver
		 * .findElement(By.xpath("//android.view.ViewGroup/android.widget.TextView[1]"))
		 * ; System.out.println(AccAndDobMisMatchElement.getText());
		 * //Assert.assertTrue(AccAndDobMisMatchElement.isDisplayed());
		 * //Assert.assertEquals(AccAndDobMisMatchElement.getText(),
		 * "Account Number and Date of Birth Mismatch");
		 * 
		 * Thread.sleep(800); WebElement retryButton = driver.findElement(By.xpath(
		 * "//android.widget.Button[@content-desc=\"Retry\"]/android.widget.TextView"));
		 * SeleniumUtils.doClick(driver, retryButton);
		 */
		// Verifying valid scenario

		SeleniumUtils.sendKeys(driver, accountNumberInputBox, "123456789098");

		SeleniumUtils.doClick(driver, nextButton);
		Thread.sleep(800);
		dateOfBirthInputBox.click();
		Thread.sleep(1000);
		String pMonthXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[1]/android.widget.Button[1]";
		String cMonthXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[1]/android.widget.EditText";

		String pDayXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[2]/android.widget.Button[1]";
		String cDayXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[2]/android.widget.EditText";

		String pYearXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[3]/android.widget.Button[1]";
		String cYearXpath = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.NumberPicker[3]/android.widget.EditText";

		SeleniumUtils.scrollForDate(driver, cMonthXpath, pMonthXpath, "October");
		SeleniumUtils.sleepForWhile(2);

		SeleniumUtils.scrollForDate(driver, cDayXpath, pDayXpath, "3");
		SeleniumUtils.sleepForWhile(2);

		driver.findElement(By.id("android:id/button1")).click();

		SeleniumUtils.doClick(driver, nextButton);

		WebElement verifyItsYouElement = driver
				.findElement(By.xpath("//android.view.ViewGroup/android.widget.TextView[1]"));
		String msg = verifyItsYouElement.getText();

		Assert.assertEquals(msg, "Verify it's you");

		SeleniumUtils.doClick(driver, nextButton);

		Thread.sleep(20000);

	}
}
