package com.unisys.udb.testrailmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Reporter;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;
import com.unisys.udb.utils.ApplicationPropertyLoader;

 

 

public class TestRailTestRunManager {

	String testPlanId = null; //Test plan id from Test Rail to include the test run.
	String testRunName = null;
	String userName = null;
	String password = null;

	static final String TEST_RAIL_URL = "https://rbs2.testrail.io";
	static final String ENTRIES_COLLECTION_NAME = "entries";
	static final String ID = "id";
	static final String UPDATE_TEST_RUN_BASE_URL = "update_plan_entry/";

	APIClient client = null;

	/*
	 * testRunEntryId is fetched from get_test_plan test rail API response by matching with
	 * name of the entries. For example, get_test_plan API response where entries collection
	 * looks like entries {id ="name of the test run underneath the test plan"}Hence, the test run name should be passed from environment properties
	 */
	String testRunEntryId = null;
	public TestRailTestRunManager() {
		client = new APIClient(TEST_RAIL_URL);
		client.setUser(ApplicationPropertyLoader.getPropertyByName("test_rail_login_user"));
		client.setPassword(ApplicationPropertyLoader.getPropertyByName("test_rail_login_password"));
		testPlanId = ApplicationPropertyLoader.getPropertyByName("TestRailTestPlanID").substring(1);
		testRunName = ApplicationPropertyLoader.getPropertyByName("TestRunName");

	}

	
	/**
	 * Retrieve test plan details for the given test plan id. The test plan id is
	 * the root to find the list of test runs which are contained with in entries collection
	 * @return JSONObject returns the response from get plan API
	 * @throws IOException, APIException if API calls failed at invoking TestRail or incomplete API json request
	 */
	private JSONObject getTestPlan() throws IOException, APIException {
		String getTestPlanAPIURL = "get_plan/" + testPlanId;
		return (JSONObject) client.sendGet(getTestPlanAPIURL);
	}

	private String getEntryId(JSONObject jsonObject,String collectionName, String matchKey, String key) {
		String cacheId = null;
		JSONArray jsonArray = null;
		if(collectionName != null) {
			jsonArray = (JSONArray) jsonObject.get(collectionName);
			@SuppressWarnings("unchecked")
			Iterator<Object> iterator = jsonArray.iterator();
			while(iterator.hasNext()) {
				JSONObject object = (JSONObject)iterator.next();
				if(String.valueOf(object.get("name")).contains((matchKey))) {
					cacheId = String.valueOf(object.get(key));
					break;
				}
			}
		}
		return cacheId;
	}

	/**
	 * Method to pull and associate the required regression test cases into the given
	 * test run programmatically.
	 * It needs the test plan id, test run name which would be pre-defined and passed
	 * through the environment properties
	 * @throws APIException
	 * @throws IOException
	 */
	public void addTestCasesIntoTestRun() throws IOException, APIException, ParseException {
		//Get the test plan details
		JSONObject testPlanResponse = this.getTestPlan();
		//Parse the json response from the above API and get test run entry id to update  test cases
		String entryId = this.getEntryId(testPlanResponse, ENTRIES_COLLECTION_NAME, testRunName, ID);
		String updateTestRunURL = UPDATE_TEST_RUN_BASE_URL + "/" + testPlanId;
		updateTestRunURL = updateTestRunURL+ "/" +  entryId;
		Object requestDataObj = this.getUpdateTestPlanRequestData();
		//Update the list of test cases into the given test run
		client.sendPost(updateTestRunURL,requestDataObj);
	}

	

	/**
	 * This will prepare the json request for update test run API
	 * @return JSONObject request body for update_plan_entry API
	 */
	public JSONObject getUpdateTestPlanRequestData() throws ParseException, IOException {
		 StringBuilder testRunData = new StringBuilder(); testRunData.append("{");
		 testRunData.append("\"suite_id\":2,");
		 testRunData.append("\"include_all\": false,");
		 String testDataCSVFolder = System.getProperty("user.dir") +
											File.separator + "src" + File.separator +"test" +
											File.separator +"resources"+
											File.separator +"testdata";
		 ArrayList<String> list = (ArrayList<String>)this.getTestCaseIds(testDataCSVFolder);
		 Set<String> details = new HashSet<String>(list);
		 System.out.println(details);
		 String caseIds = this.buildCaseIdsArray(list);
		 testRunData.append("\"case_ids\":"+ caseIds);
		 testRunData.append("}");
		 return this.getJSONFromString(testRunData.toString());

	}

	
	private JSONObject getJSONFromString(String jsonContent) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		return (JSONObject) jsonParser.parse(jsonContent);
	}

	public List<String> getTestCaseIds(String folderName) throws IOException {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		File directoryName = new File(folderName);
		File[] listOfFiles = null;
		ArrayList<String> list = new ArrayList<>();

		try {
			String tmpValue = null;
			ArrayList<String> headerList = null;
			ArrayList<String> detailList = null;
			listOfFiles = directoryName.listFiles();
			File fileName = null;

			for(int index =0; index < listOfFiles.length; index++) {
				fileName = listOfFiles[index];
				fileReader = new FileReader(fileName);
				bufferedReader = new BufferedReader(fileReader);
				int rowCount = 0;
				int testCaseNoIndex = 0;
				headerList = new ArrayList<>();
				detailList = new ArrayList<>();
				while((tmpValue = bufferedReader.readLine())!= null) {
					if(rowCount == 0) {
						this.getListFromStringValues(headerList,tmpValue);
						testCaseNoIndex = headerList.indexOf("TestCaseNumber");
						rowCount++;
						if(testCaseNoIndex == -1) {
							Reporter.log("File name missed with In_TestCase_No mandatory field from input CSV file " + fileName );
							break;
						}
					} else {
						if(tmpValue.startsWith("\"")) {
							String[] valuesInQuotes = StringUtils.substringsBetween(tmpValue , "\"", "\"");
							String[] arrOfStr = valuesInQuotes[0].split(",");
							for(int count=0;count<arrOfStr.length;count++) {
								this.getFormatedTestCaseIds(list,arrOfStr[count]);
							}
						}else {
						this.getListFromStringValues(detailList,tmpValue);
						String testCaseNo = detailList.get(testCaseNoIndex);
						this.getFormatedTestCaseIds(list,testCaseNo);
						detailList.removeAll(detailList);
						}
					}
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
				if(fileReader != null) fileReader.close();
			} catch (NullPointerException | IOException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}

	public String buildCaseIdsArray(List<String> list) {
		StringBuilder builder =  new StringBuilder("[");
		for(int index=0; index<list.size(); index++) {
			builder.append(list.get(index).substring(1));
			if(index != (list.size()-1)) {
				builder.append(",");
			}
		}
		builder.append("]");
		return builder.toString();

	}

	public  void getListFromStringValues(List<String> list, String value) {
		StringTokenizer tokenizer = new StringTokenizer(value,",");
		while(tokenizer.hasMoreTokens()) {
			String tmpValue = tokenizer.nextToken();
			list.add(tmpValue.trim());
		}
	}

	public  void getFormatedTestCaseIds(List<String> list, String value) {
		value = value.replace("\"", "");
		value = value.replace("-", ",");
		value = value.replace("_", ",");
		StringTokenizer tokenizer = new StringTokenizer(value,",");
		while(tokenizer.hasMoreTokens()) {
			String tmpValue = tokenizer.nextToken();
			list.add(tmpValue.trim());
		}
	}

	public static void main(String...args) throws Exception {
		TestRailTestRunManager testRunManager = new TestRailTestRunManager();
		testRunManager.addTestCasesIntoTestRun();
	}
}