package com.unisys.udb.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.testng.Reporter;

public class FileIOHandler {
	private FileIOHandler() {
		//prevent object instantiation from outside	
	}
	
	/**
	 * This method picks up all the files from the given source directory & compresses to a zip file.
	 * The zip file name would be as given in the reference variable
	 * @param source directory where all individual files located
	 * @param fileName name of the file. The file extension is .zip 
	 * @return boolena value true if zip file is created successfully otherwise false
	 */
	public static boolean zipFiles(String source, String fileName)  {
		File sourceFile = new File(source);
		String zipFileName = fileName;
		String destinationFilePath = sourceFile.getParent() + File.separator + zipFileName;
		File destinationFile = new File(destinationFilePath);//File name after compression
		boolean isSuccess = false; // whether the compression is successful

		if(!destinationFile.exists()) {
			ZipOutputStream out = null;
			CheckedOutputStream cos = null;
			FileOutputStream fout = null;
			try{
				fout = new FileOutputStream(destinationFile);
				cos = new CheckedOutputStream(fout, new CRC32());
				out = new ZipOutputStream(cos);
				zip(sourceFile, out, "", true);
				isSuccess = true;
			}catch(Exception ex){
				ex.printStackTrace();
			}finally{
				try {
					if(out != null) {
						out.flush();
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * Compress zip file
	 * @param file compressed file object
	 * @param out output ZIP stream
	 * @param dir relative parent directory name
	 * @param boo Whether to compress the empty directory into it
	 */
	public static void zip(File file,ZipOutputStream out,String dir,boolean boo) throws IOException{
		if(file.isDirectory()){// is the directory
			File []listFile = file.listFiles();//Get all the file objects in the directory
			if(listFile.length == 0 && boo){//empty directory compression
				out.putNextEntry(new ZipEntry(file.getName() + "/"));// put the entity into the output ZIP stream
			}else{
				for(File cfile: listFile){
					if(!cfile.getName().contains(".zip"))
						zip(cfile,out,dir + file.getName() + "/",boo);//recursive compression
				}
			}

		}else if(file.isFile()){//is a file
			byte[] bt = new byte[2048*2];
			ZipEntry ze = new ZipEntry(file.getName());//Build a compressed entity
			// Set the file size before compression
			ze.setSize(file.length());
			out.putNextEntry(ze);//// put the entity into the output ZIP stream
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(file);
				int i=0;
				while((i = fis.read(bt)) != -1) {// Loop reads and writes to the output Zip stream
					out.write(bt, 0, i);
				}
			}catch(IOException ex){
				throw new IOException("An exception occurred while writing to a compressed file", ex);
			}finally{
				try{
					if (fis != null)
						fis.close();//Close the input stream
				}catch(IOException ex){
					Reporter.log("An exception occurred while closing the input stream");
					ex.printStackTrace();
				}
			}           
		}
	}
	
	public static boolean deleteFiles(String filePath) {
		boolean isDeleted = true;
		try {
			File file = new File(filePath);
			File[] fileList = file.listFiles();
			for (File tmpFile : fileList) {
				if(tmpFile.isDirectory()) {
					deleteFiles(tmpFile.getAbsolutePath());
				} else {
					Path path = Paths.get(tmpFile.getAbsolutePath());
					Files.delete(path);
				}
			}
		} catch(Exception ex) {
			//Ignore the exception if system failes to clean the folder
			isDeleted = false;
			Reporter.log("Exception occured due to " + ex.getMessage());
		}
		return isDeleted;
	}
	public static void main(String[] args) {
		String sourceFolder = "C:\\Users\\BuddeesR\\UI_MobileAutomation\\Mobile_UIAutomation\\test-output\\testReports\\C2";
		String fileName = "C2.zip";
		FileIOHandler.zipFiles(sourceFolder, fileName);
	}
 }