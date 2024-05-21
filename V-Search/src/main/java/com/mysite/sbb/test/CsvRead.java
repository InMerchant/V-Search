package com.mysite.sbb.test;

import java.util.*;
import java.io.*;

public class CsvRead {
	public List<List<String>> readCsv() {
		List<List<String>> csvList = new ArrayList<List<String>>();
		File csv = new File("C:\\Users\\user\\Desktop\\viddata2.csv");
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(csv));
			while ((line = br.readLine()) != null) {
				List<String> cLine = new ArrayList<String>();
				String[] lineArray = line.split(",");
				cLine = Arrays.asList(lineArray);
				csvList.add(cLine);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return csvList;
	}

	public static void main(String[] args) {
		CsvRead csvRead = new CsvRead();
		List<List<String>> csvData = csvRead.readCsv();

		for (List<String> row : csvData) {
			for (String field : row) {
				System.out.print(field + " ");
			}
			System.out.println();
		}
	}
}
