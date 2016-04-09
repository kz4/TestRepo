package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.antlr.runtime.debug.BlankDebugEventListener;
import org.apache.commons.lang3.StringUtils;


public class Test5 {

	private static String temp_file = "C:\\Users\\zhangka\\Desktop\\BufferWriter.txt";
	private static String long_file = "C:\\Users\\zhangka\\Desktop\\Long text.txt";

	private static String index_base = "/Users/kaichenzhang/Desktop/invertedindex/index";


	static String newline_ = System.getProperty("line.separator");
	
	public static void main(String[] args) {    
		
		System.out.println("1");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("2");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("3");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("4");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("5");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		List<String> lst = new ArrayList<String>();
		lst.add("a");
		lst.add("b");
		lst.add("c");
		String s = StringUtils.join(lst, "\n");
		System.out.println(s);

		Map<String, Map<Integer, List<Integer>>> term_DocId_Posn_LstOfPosn = new HashMap<String, Map<Integer,List<Integer>>>();

		List<Integer> underminingPosn = new ArrayList<Integer>();
		underminingPosn.add(0);
		underminingPosn.add(1);
		underminingPosn.add(4);
		underminingPosn.add(24);
		underminingPosn.add(52);
		underminingPosn.add(60);

		List<Integer> underminingPosn2 = new ArrayList<Integer>();
		underminingPosn2.add(2);
		underminingPosn2.add(4);
		underminingPosn2.add(7);
		underminingPosn2.add(65);
		underminingPosn2.add(66);
		underminingPosn2.add(87);

		Map<Integer, List<Integer>> docId_lstOfPosn = new HashMap<Integer, List<Integer>>();
		docId_lstOfPosn.put(0, underminingPosn);
		docId_lstOfPosn.put(4, underminingPosn2);

		term_DocId_Posn_LstOfPosn.put("undermining", docId_lstOfPosn);
		List<Integer> thePosn = new ArrayList<Integer>();
		thePosn.add(2);
		thePosn.add(21);
		thePosn.add(44);
		thePosn.add(52);
		thePosn.add(602);

		List<Integer> thePosn2 = new ArrayList<Integer>();
		thePosn2.add(0);
		thePosn2.add(2);
		thePosn2.add(4);
		thePosn2.add(5);
		thePosn2.add(7);

		Map<Integer, List<Integer>> the_docId_lstOfPosn = new HashMap<Integer, List<Integer>>();
		the_docId_lstOfPosn.put(3, thePosn);
		the_docId_lstOfPosn.put(4, thePosn2);

		term_DocId_Posn_LstOfPosn.put("the", the_docId_lstOfPosn);
		List<Integer> carPosn = new ArrayList<Integer>();
		carPosn.add(2);
		carPosn.add(24);
		carPosn.add(44);
		carPosn.add(242);
		carPosn.add(524);
		carPosn.add(604);

		List<Integer> carPosn2 = new ArrayList<Integer>();
		carPosn2.add(2);
		carPosn2.add(3);
		carPosn2.add(4);
		carPosn2.add(24);
		carPosn2.add(52);
		carPosn2.add(60);
		
		List<Integer> carPosn3 = new ArrayList<Integer>();
		carPosn3.add(0);
		carPosn3.add(2);
		carPosn3.add(4);
		carPosn3.add(24);
		carPosn3.add(52);
		carPosn3.add(64);

		Map<Integer, List<Integer>> car_docId_lstOfPosn = new HashMap<Integer, List<Integer>>();
		car_docId_lstOfPosn.put(0, carPosn);
		car_docId_lstOfPosn.put(3, carPosn2);
		car_docId_lstOfPosn.put(9, carPosn3);
		term_DocId_Posn_LstOfPosn.put("car", car_docId_lstOfPosn);
		
		//createInvertedIndex(term_DocId_Posn_LstOfPosn, 0);

		String index0Path = "/Users/kaichenzhang/Desktop/invertedindex/index0.txt";
		
		String termUndermining = readFileByBytes(index0Path, 0, 50);
		String termThe = readFileByBytes(index0Path, 50, 37);
		String termCar = readFileByBytes(index0Path, 87, 66);

		String index15Path = "/Users/kaichenzhang/Desktop/invertedindex/index15.txt";
		
		String term03 = readFileByBytes(index15Path, 1805, 300);	//0.3
		String termClock = readFileByBytes(index15Path, 2562959, 316);	//clock
		String injured = readFileByBytes(index15Path, 5344413, 1748);
		
		String term0028 = readFileByBytes(index0Path, 0, 14);
		String term008 = readFileByBytes(index0Path, 32, 14);
		String term000 = readFileByBytes(index0Path, 46, 391);   	
		
		System.out.println(termUndermining);
		System.out.println(termThe);
		System.out.println(termCar);
	}

	/**
	 * 
	 * @param path - Path to the file that will be read
	 * @param offset - starting position in byte of the term that needs to be read
	 * @param len - = (end - start) in byte
	 * @return The term, docId, term frequency and positions in the documents
	 * For example:
	 * car
		0 6 2 24 44 242 524 604 
		3 6 2 3 4 24 52 60 
		9 6 0 2 4 24 52 64 

	 */
	private static String readFileByBytes(String path, int offset, int len) {

		//create file object
		File file = new File(path);
		
		// For example:
//		car
//		0 6 2 24 44 242 524 604 
//		3 6 2 3 4 24 52 60 
//		9 6 0 2 4 24 52 64 
		String termBlock = "";
		
		try
		{
			//create FileInputStream object
			FileInputStream fin = new FileInputStream(file);
			System.out.println("In catalog file, trying to read: " + file);
			
			/*
			 * To skip n bytes while reading the file, use
			 * int skip(int nBytes) method of Java FileInputStream class.
			 *
			 * This method skip over n bytes of data from stream. This method returns
			 * actual number of bytes that have been skipped.
			 */
			//skip first start bytes
			fin.skip(offset);
			byte[] res = new byte[len];

			// second argument is the offset for res, so in our case always 0
			fin.read(res, 0, len);
			termBlock = new String(res, "UTF-8");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found" + e);
		}
		catch(IOException ioe)
		{
			System.out.println("Exception while reading the file " + ioe);
		}
		return termBlock;
	}

	private static void createInvertedIndex(Map<String, Map<Integer, List<Integer>>> term_DocId_Posn_LstOfPosn, int i) {

		String fn = "/Users/kaichenzhang/Desktop/invertedindex/index" + i + ".txt";		
		String fn_cat = "/Users/kaichenzhang/Desktop/catalog/cat" + i + ".txt";
		BufferedWriter out_index = null;
		BufferedWriter out_cat = null;

		int start = 0;
		int end = 0;
		
		try
		{   
			//Open the output index file.
			FileWriter ostream = new FileWriter(fn);
			out_index = new BufferedWriter(ostream);
			System.out.println("Writing out index: " + fn);
			StringBuilder block = new StringBuilder();
			
			//Open the output catalog file.
			FileWriter ostream_cat = new FileWriter(fn_cat);
			out_cat = new BufferedWriter(ostream_cat);
			StringBuilder block_cat = new StringBuilder();
			
			StringBuilder one_term_block = null;

			for (String term : term_DocId_Posn_LstOfPosn.keySet()) {
				// Write to the catalog file
				// starting position				
				start = end;
				block_cat.append(term + " " + start + " ");
												
				block.append(term);
				block.append(newline_);
				one_term_block = new StringBuilder();
				one_term_block.append(term);
				one_term_block.append(newline_);
				

				for (int docId : term_DocId_Posn_LstOfPosn.get(term).keySet()) {
					block.append(docId + " ");
					block.append(term_DocId_Posn_LstOfPosn.get(term).get(docId).size() + " ");
					one_term_block.append(docId + " ");
					one_term_block.append(term_DocId_Posn_LstOfPosn.get(term).get(docId).size() + " ");
//					for (int posn : term_DocId_Posn_LstOfPosn.get(term).get(docId)) {						
//						block.append(posn);
//						block.append(" ");
//						one_term_block.append(posn);
//						one_term_block.append(" ");
//					}
					int lst_length = term_DocId_Posn_LstOfPosn.get(term).get(docId).size();
					int counter = 0;
					for (int posn : term_DocId_Posn_LstOfPosn.get(term).get(docId)) {						
						block.append(posn);												
						one_term_block.append(posn);
						
						if (counter < lst_length - 1){
							block.append(" ");
							one_term_block.append(" ");
						}
						counter++;
					}
					block.append(newline_);
					one_term_block.append(newline_);
				}

				// Write to the catalog file
				// offset position
				int byteLength = block.toString().getBytes().length;
				end = byteLength;
				int byteLength_diff = one_term_block.toString().getBytes().length;
				//end = byteLength;
//				block_cat.append(end);
				block_cat.append(byteLength_diff);
				block_cat.append(newline_);
				
				// Before writing to the next term block, set the
				// start bytes of new term to the end bytes of the 
				// old term
				start = end; 

			}
			String one_map = block.toString();
			out_index.write(one_map);
			
			// Write to the catalog file
			String catStr = block_cat.toString();
			out_cat.write(catStr);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally {
			try {
				out_index.flush(); 
				out_cat.flush(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out_index.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}