package com.unisys.udb.page;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.unisys.udb.report.ReportGenerator;
import com.unisys.udb.utils.SeleniumUtils;

import io.appium.java_client.android.AndroidDriver;

public class CookiesAndPrivacyPage {
	
	private AndroidDriver driver=null;
	private ReportGenerator reportGenertor = null;
	
	@FindBy(xpath="//android.widget.TextView[1]")
	WebElement  welcomeTextElement;
	
	@FindBy(xpath = "//android.widget.Button[@content-desc=\"Accept my cookies\"]")
	WebElement acceptCookiesBtn;
	
	@FindBy(xpath="//android.widget.CheckBox")
	WebElement termsAndConditionsBtn;
	
	@FindBy(id="android:id/button1")
	WebElement termsErrorOKBtn;
	
	@FindBy(xpath="//android.widget.Button[@content-desc=\"Continue\"]/android.widget.TextView")
	WebElement continueBtn;
	
	public CookiesAndPrivacyPage(AndroidDriver driver, ReportGenerator reportGenerator) {
		this.reportGenertor =reportGenerator;
		this.driver=driver;
		PageFactory.initElements(this.driver,this);
	}

	public void processTermsAndCookiesScreens(HashMap<String, String> hashmap) throws InterruptedException {
		Thread.sleep(1000);
		Assert.assertTrue(welcomeTextElement.isDisplayed());
		Thread.sleep(2000);
		reportGenertor.logAndCaptureScreenshot(driver, "CookiesScreen", "CookiesScreen Displayed", Status.PASS);
		SeleniumUtils.doClick(driver, acceptCookiesBtn);
		Thread.sleep(2000);
		reportGenertor.logAndCaptureScreenshot(driver, "TermsAndConditionsScreen", "TermsAndConditionsScreen Displayed and screenshot taken", Status.PASS);
		SeleniumUtils.doClick(driver, continueBtn);
		Thread.sleep(2000);
		reportGenertor.logAndCaptureScreenshot(driver, "TermsAndConditionsErrorPopupScreen", "TermsAndConditionsErrorPopupScreen Displayed and screenshot taken", Status.PASS);
		SeleniumUtils.doClick(driver, termsErrorOKBtn);
		
		SeleniumUtils.doClick(driver, termsAndConditionsBtn);
		SeleniumUtils.doClick(driver, continueBtn);
		Assert.assertTrue(welcomeTextElement.isDisplayed());
		Thread.sleep(2000);
		reportGenertor.logAndCaptureScreenshot(driver, "Welcome TO Unibank Screen", "Welcome TO Unibank Screen Displayed and screenshot taken", Status.PASS);
		System.out.println();
	}

}
