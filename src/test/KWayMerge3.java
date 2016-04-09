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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;


public class KWayMerge3
{
	private static String finalIndexPath_ = "/Users/kaichenzhang/Desktop/finalIndex.txt";
	private static String finalCatalogPath_ = "/Users/kaichenzhang/Desktop/finalCatalog.txt";
	
	public static List<String> merge(List<List<Term_Offset_Len>> sortedCatalogs) throws IOException {
		// estimate length
		int size = 0;
		for (List<Term_Offset_Len> list : sortedCatalogs) {
			size += list.size();
		}
		
		List<String> output = new ArrayList<String>(size);
		int sortedListsize = sortedCatalogs.size();
		
		// fileindex, index, term
		Queue<Catalog> queue = new PriorityQueue<Catalog>(sortedListsize);
		
		// Initialization, fill 25 nodes with the first term block from
		// the catalog
		// counter is the name of the list, that goes from 0 to 24
		// index is the position of the term in the list of blocks
		// term is current term that is polled
		int catNo = 0;
		int index = 0;
		for (List<Term_Offset_Len> catalog : sortedCatalogs) {
//			queue.add(new Catalog(catNo, index, catalog.get(index).term_, catalog.get(index).offset_, catalog.get(index).len_));
			queue.add(new Catalog(catNo, index, catalog.get(index).term_));
			catNo++;
		}

		//Catalog temp = new Catalog(-1, -1, "", -1, -1);
		
//		String finalIndex = "";
		
		BufferedWriter out_index = null;
		BufferedWriter out_cat = null;
		
		FileWriter ostream = new FileWriter(finalIndexPath_);
		out_index = new BufferedWriter(ostream);
		
		FileWriter ostream_cat = new FileWriter(finalCatalogPath_);
		out_cat = new BufferedWriter(ostream_cat);
		
		int finalCatCurrentPosition = 0;
		
		String currentTermFromCatalog = "";
		List<String> docId_tf_lstOfPosn = new ArrayList<String>();
		// process until queue becomes empty
		while (!queue.isEmpty()) {
			
			// Retrieve the min term from the min heap (Priority queue)
			Catalog entry = queue.poll();
			catNo = entry.catName_;
			index = entry.index_;
			String term = sortedCatalogs.get(catNo).get(index).term_;
			int offset = sortedCatalogs.get(catNo).get(index).offset_;
			int len = sortedCatalogs.get(catNo).get(index).len_;
			
			System.out.println(term);
			
			// Change to constatns.
			// catNo is the same as invertedIndexNo
			String partialInvertedIndexPath = "/Users/kaichenzhang/Desktop/invertedindex/index" + catNo + ".txt";
			
			String termBlock = readFileByBytes(partialInvertedIndexPath, offset, len);
			// In the first time, initialize the temp to the first
			// termblock of the catalog
			if(currentTermFromCatalog.equals("")){
				currentTermFromCatalog = entry.term_;
				
				
				docId_tf_lstOfPosn.add(termBlock);
				
				output.add(currentTermFromCatalog);
	
			} else if (currentTermFromCatalog.equals(entry.term_)){
				//combine duplicated term's docID info
				// retrieve from partial index using the offset and len and then add it to the arrayList 
				
				String[] termBlockSplit = termBlock.split("\n", 2);		
				String termBlockWithoutTerm = termBlockSplit[1];
				docId_tf_lstOfPosn.add(termBlockWithoutTerm);
				
				output.add(currentTermFromCatalog);
			} else {
				// Write temp to disk and put the current
				// entry into temp
				
//				docId_tf_lstOfPosn write to disk
				
				//Open the output index file.
				
				String finalIndexTermBlock = StringUtils.join(docId_tf_lstOfPosn, "");
				int finalIndexTermBlockLength = finalIndexTermBlock.getBytes().length;
				out_index.write(finalIndexTermBlock);
//				out_index.write("\n");
				
				out_cat.write(currentTermFromCatalog + " " + finalCatCurrentPosition + " " + finalIndexTermBlockLength);
				out_cat.write("\n");
				finalCatCurrentPosition += finalIndexTermBlockLength;
				
				docId_tf_lstOfPosn.clear();
				
				currentTermFromCatalog = entry.term_;
				docId_tf_lstOfPosn.add(termBlock);
				output.add(currentTermFromCatalog);
			}
			
			if (index < sortedCatalogs.get(catNo).size() - 1){
				index++;
				// Add the next term from that catalog to the queue
//				queue.add(new Catalog(catNo, index, sortedCatalogs.get(catNo).get(index).term_, sortedCatalogs.get(catNo).get(index).offset_, sortedCatalogs.get(catNo).get(index).len_));
				entry.index_ = index;
				entry.term_ = sortedCatalogs.get(catNo).get(index).term_;
//				entry.offset_ = sortedCatalogs.get(catNo).get(index).offset_;
//				entry.len_ = sortedCatalogs.get(catNo).get(index).len_;
				queue.add(entry);
			} 
		}
		
		// For the case after the queue is empty, but
		// never get a chance to write docId_tf_lstOfPosn to disk
		if (docId_tf_lstOfPosn.size() > 0){
			// write currentTerm to the index file
			String finalIndexTermBlock = StringUtils.join(docId_tf_lstOfPosn, "");
			int finalIndexTermBlockLength = finalIndexTermBlock.getBytes().length;
			out_index.write(finalIndexTermBlock);

			
			out_cat.write(currentTermFromCatalog + " " + finalCatCurrentPosition + " " + finalIndexTermBlockLength);
			out_cat.write("\n");
			finalCatCurrentPosition += finalIndexTermBlockLength;
		}

		return output;
	}

	public static void main(String[] args) {
		
//		List<Term_Offset_Len> cat1 = new ArrayList<Term_Offset_Len>();
//		Term_Offset_Len t1 = new Term_Offset_Len("alleg", 0, 49);
//		Term_Offset_Len t2 = new Term_Offset_Len("cat", 0, 49);
//		Term_Offset_Len t3 = new Term_Offset_Len("elephant", 0, 49);
//		Term_Offset_Len t4 = new Term_Offset_Len("gang", 0, 49);
//		Term_Offset_Len t41 = new Term_Offset_Len("ginger", 0, 49);
//		cat1.add(t1);
//		cat1.add(t2);
//		cat1.add(t3);
//		cat1.add(t4);
//		cat1.add(t41);
//		
//		List<Term_Offset_Len> cat2 = new ArrayList<Term_Offset_Len>();
//		Term_Offset_Len t5 = new Term_Offset_Len("boy", 0, 49);
//		Term_Offset_Len t6 = new Term_Offset_Len("cat", 0, 49);
//		Term_Offset_Len t7 = new Term_Offset_Len("dog", 0, 49);
//		Term_Offset_Len t8 = new Term_Offset_Len("fin", 0, 49);
//		Term_Offset_Len t9 = new Term_Offset_Len("hazard", 0, 49);
//		cat2.add(t5);
//		cat2.add(t6);
//		cat2.add(t7);
//		cat2.add(t8);
//		cat2.add(t9);
//		
//		List<Term_Offset_Len> cat3 = new ArrayList<Term_Offset_Len>();
//		Term_Offset_Len t10 = new Term_Offset_Len("alleg", 0, 49);
//		Term_Offset_Len t11 = new Term_Offset_Len("apple", 0, 49);
//		Term_Offset_Len t12 = new Term_Offset_Len("jelly", 0, 49);
//		Term_Offset_Len t13 = new Term_Offset_Len("king", 0, 49);
//		Term_Offset_Len t14 = new Term_Offset_Len("lizard", 0, 49);
//		cat3.add(t10);
//		cat3.add(t11);
//		cat3.add(t12);
//		cat3.add(t13);
//		cat3.add(t14);
//
//		List<List<Term_Offset_Len>> lists = new ArrayList<List<Term_Offset_Len>>();
//		lists.add(cat1);
//		lists.add(cat2);
//		lists.add(cat3);
		
		KWayMerge3 k = new KWayMerge3();
		
		List<List<Term_Offset_Len>> lists = k.read_partial_catalog();

		List<String> output = null;
		try {
			output = merge(lists);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for (String term : output) {
//			System.out.println(term);
//		}
		
	}
	
	private List<List<Term_Offset_Len>> read_partial_catalog(){
		List<List<Term_Offset_Len>> res = new ArrayList<List<Term_Offset_Len>>();
		int j = 0;
		//List<File> files = Parser.getFiles("/Users/kaichenzhang/Desktop/catalog/", null);
		for (int i = 0; i < 25; i++) {
			String fn =  "/Users/kaichenzhang/Desktop/catalog/cat" + j + ".txt";
//			String fn =  "/Users/kaichenzhang/Desktop/catalogtest/cat" + j + ".txt";
			List<Term_Offset_Len> lst_term_offset_len = read_one_catalog_file(fn);
			res.add(lst_term_offset_len);
			j++;
		}
		
		return res;
	}
	
	private static List<Term_Offset_Len> read_one_catalog_file(String file) {

		//List<XContentBuilder> lstBuilder = new ArrayList<XContentBuilder>();

		
		List<Term_Offset_Len> lst_term_offset_len = new ArrayList<Term_Offset_Len>();
		String line = null;
		StringBuilder sb;
		BufferedReader br = null;
		try {

			br = new BufferedReader(new FileReader(file));
			sb = new StringBuilder();
			line = br.readLine();
//			Boolean readingText = false;
//			String docno = null;
//			String text = "";

			while (line != null) {
				String[] s = line.split("\\s+");
				Term_Offset_Len t = new Term_Offset_Len(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]));
				lst_term_offset_len.add(t);
//	            sb.append(line);
//	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        //String everything = sb.toString();

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lst_term_offset_len;
	}

	private static String readFileByBytes(String path, int start, int len) {
		File file = new File(path);
		
		// For example:
//		car
//		0 6 2 24 44 242 524 604 
//		3 6 2 3 4 24 52 60 
//		9 6 0 2 4 24 52 64 
		String termBlock = "";
		
		FileInputStream fin = null;
		try
		{
			//create FileInputStream object
			fin = new FileInputStream(file);
//			System.out.println("In catalog file, trying to read: " + file);
			
			/*
			 * To skip n bytes while reading the file, use
			 * int skip(int nBytes) method of Java FileInputStream class.
			 *
			 * This method skip over n bytes of data from stream. This method returns
			 * actual number of bytes that have been skipped.
			 */
			//skip first start bytes
			fin.skip(start);
			byte[] res = new byte[len];

			// second argument is the offset for res, so in our case always 0
			fin.read(res, 0, len);
			termBlock = new String(res, "UTF-8");
			fin.close();
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
}
