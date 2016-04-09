package test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;


public class KwayMerge2
{
//	public static List<Term_Offset_Len> merge(List<List<Term_Offset_Len>> sortedCatalogs) {
	public static List<String> merge(List<List<Term_Offset_Len>> sortedCatalogs) {
		// estimate length
		int size = 0;
		for (List<Term_Offset_Len> list : sortedCatalogs) {
			size += list.size();
		}
		
//		List<Term_Offset_Len> output = new ArrayList<Term_Offset_Len>(size);
		List<String> output = new ArrayList<String>(size);
		int sortedListsize = sortedCatalogs.size();
		
		// fileindex, index, term
		Queue<Entry> queue = new PriorityQueue<Entry>(sortedListsize);

		// read first entry from each source
//		for (List<Term_Offset_Len> list : sortedLists) {
//			Iterator<Term_Offset_Len> it = list.iterator();
//			if (it.hasNext()) {
//				queue.add(new Entry(it.next(), it));
//			}
//		}
		
		// Initialization, fill 25 nodes with the first term block from
		// the catalog
		// counter is the name of the list, that goes from 0 to 24
		// index is the position of the term in the list of blocks
		// term is current term that is polled
		int lstName = 0;
		int index = 0;
		for (List<Term_Offset_Len> catalog : sortedCatalogs) {			
			queue.add(new Entry(lstName, index, catalog.get(index).term_));
			lstName++;
		}

		Entry temp = new Entry(-1, -1, "");
		
		// process until queue becomes empty
		while (!queue.isEmpty()) {
			Entry entry = queue.poll();
			lstName = entry.lstName_;
			index = entry.index_;
			String term = sortedCatalogs.get(lstName).get(index).term_;
			
			
			
			// In the first time, initialize the temp to the first
			if(temp.term_.equals("")){
				temp = entry;
				
				output.add(temp.term_);
				if (index < sortedCatalogs.get(lstName).size() - 1){
					index++;
					// Add the next term from that catalog to the queue
					queue.add(new Entry(lstName, index, sortedCatalogs.get(lstName).get(index).term_));
				}
				continue;
			} else if (temp.term_.equals(entry.term_)){
				//combine duplicated term's docID info
				output.add(temp.term_);
			} else {
				// Write temp to disk and put the current
				// entry into temp
				temp = entry;
				output.add(temp.term_);
			}
			
			if (index < sortedCatalogs.get(lstName).size() - 1){
				index++;
				// Add the next term from that catalog to the queue
				queue.add(new Entry(lstName, index, sortedCatalogs.get(lstName).get(index).term_));
			}
		}

		return output;
	}

	public static void main(String[] args) {
		
		List<Term_Offset_Len> cat1 = new ArrayList<Term_Offset_Len>();
		Term_Offset_Len t1 = new Term_Offset_Len("alleg", 0, 49);
		Term_Offset_Len t2 = new Term_Offset_Len("cat", 0, 49);
		Term_Offset_Len t3 = new Term_Offset_Len("elephant", 0, 49);
		Term_Offset_Len t4 = new Term_Offset_Len("gang", 0, 49);
		cat1.add(t1);
		cat1.add(t2);
		cat1.add(t3);
		cat1.add(t4);

		
		List<Term_Offset_Len> cat2 = new ArrayList<Term_Offset_Len>();
		Term_Offset_Len t5 = new Term_Offset_Len("boy", 0, 49);
		Term_Offset_Len t6 = new Term_Offset_Len("cat", 0, 49);
		Term_Offset_Len t7 = new Term_Offset_Len("dog", 0, 49);
		Term_Offset_Len t8 = new Term_Offset_Len("fin", 0, 49);
		Term_Offset_Len t9 = new Term_Offset_Len("hazard", 0, 49);
		cat2.add(t5);
		cat2.add(t6);
		cat2.add(t7);
		cat2.add(t8);
		cat2.add(t9);
		
		List<Term_Offset_Len> cat3 = new ArrayList<Term_Offset_Len>();
		Term_Offset_Len t10 = new Term_Offset_Len("alleg", 0, 49);
		Term_Offset_Len t11 = new Term_Offset_Len("apple", 0, 49);
		Term_Offset_Len t12 = new Term_Offset_Len("jelly", 0, 49);
		Term_Offset_Len t13 = new Term_Offset_Len("king", 0, 49);
		Term_Offset_Len t14 = new Term_Offset_Len("lizard", 0, 49);
		cat3.add(t10);
		cat3.add(t11);
		cat3.add(t12);
		cat3.add(t13);
		cat3.add(t14);

		List<List<Term_Offset_Len>> lists = new ArrayList<List<Term_Offset_Len>>();
		lists.add(cat1);
		lists.add(cat2);
		lists.add(cat3);

		List<String> output = merge(lists);
		for (String term : output) {
			System.out.println(term);
		}
		
	}
	
	private List<List<Term_Offset_Len>> read_partial_catalog(){
		List<List<Term_Offset_Len>> res = new ArrayList<List<Term_Offset_Len>>();
		List<File> files = Parser.getFiles("/Users/kaichenzhang/Desktop/catalog/", null);
		for (int i = 0; i < files.size(); i++) {
			List<Term_Offset_Len> lst_term_offset_len = read_one_catalog_file(files.get(i));
			res.add(lst_term_offset_len);
		}
		
		return res;
	}
	
	private static List<Term_Offset_Len> read_one_catalog_file(File file) {

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
}
