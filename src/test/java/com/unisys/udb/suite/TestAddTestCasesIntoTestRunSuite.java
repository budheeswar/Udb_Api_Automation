package com.unisys.udb.suite;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.unisys.udb.testrailmanager.TestRailTestRunManager;


public class TestAddTestCasesIntoTestRunSuite {

	public TestAddTestCasesIntoTestRunSuite() {
	}

	@Test(priority = 1, groups = { "Prerequisite" }, testName = "Loading test cases into test run")
	public void loadTestCasesIntoTestRun() {
		try {
			TestRailTestRunManager testRunManager = new TestRailTestRunManager();
			testRunManager.addTestCasesIntoTestRun();
			Assert.assertTrue("Test Cases are added into Test Run", true);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Failed to load test cases into test run");
		}
	}

}