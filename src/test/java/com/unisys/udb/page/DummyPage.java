package com.unisys.udb.page;

import com.aventstack.extentreports.Status;
import com.unisys.udb.report.ReportGenerator;

public class DummyPage {
	
	ReportGenerator reportGenerator=null;
	
	public DummyPage(ReportGenerator gen) {
		this.reportGenerator=gen;
	}
	public void ValidLogin() {
		reportGenerator.logMessage("On Login Screen", Status.PASS);
		reportGenerator.logMessage("Entered Valid Username and Password and clicks Submit", Status.PASS);
		reportGenerator.logMessage("Land on Home Screen", Status.PASS);
	}
	public void invalidLogin() {
		reportGenerator.logMessage("On Login Screen", Status.PASS);
		reportGenerator.logMessage("Entered InValid Username and Password then clicks Submit", Status.PASS);
		reportGenerator.logMessage("Login Screen with Error Message", Status.PASS);
	}
	public void emptyLogin() {
		reportGenerator.logMessage("On Login Screen", Status.PASS);
		reportGenerator.logMessage("Entered Empty Username and Password then clicks Submit", Status.PASS);
		reportGenerator.logMessage("Login Screen with Empty values Error Message ", Status.PASS);
	}
	

}
