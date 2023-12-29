package com.unisys.udb.dataprovider;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.testng.annotations.DataProvider;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class CsvDataProvider {

	/**
	 * Gets the data provider.
	 *
	 * @param method Method is the test method which represent test case.
	 * @return an Iterator of Object[] , Internally it will contain list of hash
	 *         maps where each list element represents a hash map with key value
	 *         pair of csv parameter name and its values in a given row.
	 * 
	 *         Eg: Col1 | Col2 | Col3 Row1 A | B | C Row2 X | Y | Z
	 * 
	 *         Hashmap1 = {Col1,A} ,{Col2,B} , {Col3,C} Hashmap2 = {Col1,X}
	 *         ,{Col2,Y} , {Col3,Z}
	 * 
	 *         List = [Hashmap1 , Hashmap2]
	 * 
	 */
	public CsvDataProvider() {
		super();
	}

	@DataProvider(name = "csvDataProvider")
	public static Iterator<Object[]> getDataProvider(Method method) {
		Collection<Object[]> dp = null;
		Iterator<Object[]> iterator = null;
		try {
			System.out.println(method.getDeclaringClass());
			System.out.println(method.getName());
			Class<?> cls =  method.getDeclaringClass();
			Map<String, String> concurrentMap = null;
			String dir = System.getProperty("user.dir");
			String inputDataFileAbsPath = dir + "/src/test/resources/testdata/"+cls.getSimpleName()+"."+method.getName()+".csv";
			System.out.println("PATH "+inputDataFileAbsPath);
			List<Map<String, String>> testDataList = getDataProvider(inputDataFileAbsPath);
			dp = new CopyOnWriteArrayList<>();
			ListIterator<Map<String, String>> iter = testDataList.listIterator();
			while (iter.hasNext()) {
				concurrentMap = iter.next();
				dp.add(new Object[] { concurrentMap });
			}
			iterator = dp.iterator();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception is occured while preparing test data collection due to " + e.getMessage());
		}
		return iterator;
	}

	/**
	 * Gets the data provider.
	 *
	 * @param fileName the file name
	 * @return an Iterator of Object[] , Internally it will contain list of hash
	 *         maps where each list element represents a hash map with key value
	 *         pair of csv parameter name and its values in a given row.
	 * 
	 *         Eg: Col1 | Col2 | Col3 Row1 A | B | C Row2 X | Y | Z
	 * 
	 *         Hashmap1 = {Col1,A} ,{Col2,B} , {Col3,C} Hashmap2 = {Col1,X}
	 *         ,{Col2,Y} , {Col3,Z}
	 * 
	 *         List = [Hashmap1 , Hashmap2]
	 */

	private static List<Map<String, String>> getDataProvider(String fileName) {
		FileInputStream csvFileAsInputStream = null;
		InputStreamReader isr = null;
		List<Map<String, String>> testDataList = new ArrayList<>();
		try {
			csvFileAsInputStream = new FileInputStream(fileName);
			isr = new InputStreamReader(csvFileAsInputStream);
			// The settings object provides many configuration options
			CsvParserSettings parserSettings = new CsvParserSettings();
			parserSettings.getFormat().setDelimiter(',');
			parserSettings.getFormat().setLineSeparator("\n");
			// You can configure the parser to automatically detect what line separator
			// sequence is in the input
			parserSettings.setLineSeparatorDetectionEnabled(true);

			// A RowListProcessor stores each parsed row in a List.
			RowListProcessor rowProcessor = new RowListProcessor();

			// You can configure the parser to use a RowProcessor to process the values of
			// each parsed row.
			// You will find more RowProcessors in the
			// 'com.univocity.parsers.common.processor' package, but you can also create
			// your
			// own.
			parserSettings.setProcessor(rowProcessor);

			// Let's consider the first parsed row as the headers of each column in the
			// file.
			parserSettings.setHeaderExtractionEnabled(true);

			// creates a parser instance with the given settings
			CsvParser parser = new CsvParser(parserSettings);

			// the 'parse' method will parse the file and delegate each parsed row to the
			// RowProcessor you defined
			parser.parse(isr);

			// get the parsed records from the RowListProcessor here.
			// Note that different implementations of RowProcessor will provide different
			// sets
			// of functionalities.
			String[] headers = rowProcessor.getHeaders();
			List<String[]> rows = rowProcessor.getRows();
			for (String[] row : rows) {
				Map<String, String> map = new LinkedHashMap<>();
				int i = 0;
				for (String header : headers) {
					map.put(header, row[i] == null ? "" : row[i]);
					i++;
				}
				testDataList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (csvFileAsInputStream != null)
					csvFileAsInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return testDataList;
	}
}
