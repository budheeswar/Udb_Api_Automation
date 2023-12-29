package com.unisys.udb.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.unisys.udb.constants.UDBMobileConstants;

import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;

public class SeleniumUtils {

	public static boolean doClick(AndroidDriver driver, WebElement element) {
		boolean isClicked = false;
		try {
			WebDriverWait webWait = new WebDriverWait(driver, Duration.ofSeconds(
					Integer.parseInt(ApplicationPropertyLoader.getPropertyByName(UDBMobileConstants.JS_LOADING_TIME))));
			webWait.until(ExpectedConditions.elementToBeClickable(element));
			webWait.until(ExpectedConditions.visibilityOf(element));
			element.click();
			isClicked = true;
		} catch (Exception ex) {
			Reporter.log("Exception occured while doClick event " + ex.getMessage());
			throw ex;
		}
		return isClicked;
	}

	public static void sendKeys(AndroidDriver driver, WebElement element, String keyValue) {
		try {

			WebDriverWait webWait = new WebDriverWait(driver, Duration.ofSeconds(
					Integer.parseInt(ApplicationPropertyLoader.getPropertyByName(UDBMobileConstants.JS_LOADING_TIME))));
			webWait.until(ExpectedConditions.elementToBeClickable(element));
			webWait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			element.sendKeys(keyValue);
		} catch (Exception ex) {
			Reporter.log("Exception occured while entering value into an UI component " + ex.getMessage());
			throw ex;
		}
	}

	public static void sleepForWhile(int seconds) {
		try {
			int milliSeconds = (seconds * 1000);
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			System.out.println("Got Exception while ");
			e.printStackTrace();
		}
	}

	public static void swipeActions(String direction, AndroidDriver driver) throws Exception {

		Dimension size = driver.manage().window().getSize();

		int sWidth, sHeight, eWidth = 0, eHeight = 0;

		sWidth = size.getWidth() / 2;
		sHeight = size.getHeight() / 2;
		int border = 15;
		switch (direction.toUpperCase()) {

		case "DOWN":
			eWidth = size.getWidth() / 2;
			eHeight = border;
			break;
		case "UP":
			eWidth = size.getWidth() / 2;
			eHeight = size.getHeight() - border;
			break;
		case "RIGHT":
			eWidth = size.getWidth();
			eHeight = size.getHeight() / 2;
			break;
		case "LEFT":
			eWidth = border;
			eHeight = size.getHeight() / 2;
		default:
			throw new Exception("Invalid DIrection Entered");
		}

		try {

			TouchAction action = new TouchAction(driver);
			action.press(PointOption.point(sWidth, sHeight)).waitAction().moveTo(PointOption.point(eWidth, eHeight))
					.release().perform();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public static void scrollForDate(AndroidDriver driver,String sourceElementXPath,String destElementXPath,String targetText) {
		WebElement pMonthElement = driver.findElement(By.xpath(destElementXPath));
		WebElement cMonthElement = driver.findElement(By.xpath(sourceElementXPath));
		String k = cMonthElement.getText();
		TouchAction touchAction = new TouchAction(driver);
		Duration duration = Duration.ofMillis(4);
		while (!k.equalsIgnoreCase(targetText)) {
			touchAction.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(pMonthElement))
					.withDuration(duration)).moveTo(ElementOption.element(pMonthElement)).release().perform();
			cMonthElement = driver.findElement(By.xpath(sourceElementXPath));
			k = cMonthElement.getText();
			System.out.println(k+"/////////////////////////");
		}
	}
	

	
}