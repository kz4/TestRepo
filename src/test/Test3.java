package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.FormView;


public class Test3 {

	int termId_ = -1;
	int docId_ = -1;

	// Keep track a set of unique terms
	Set<String> terms_ = new HashSet<String>();

	// Map of termId and term
	Map<Integer, String> termId_term_ = new HashMap<Integer, String>();
	Map<String, Integer> term_termId_ = new HashMap<String, Integer>();

	// Map of docId and docno
	Map<Integer, String> docId_docno_ = new HashMap<Integer, String>();
	
	public static void main(String[] args) {

		int j = 0;
		for (int i = 0 ; i <5 ; i++){
			j++;
		}
		
		Map<Integer, Map<Integer, List<Integer>>> termId_DocId_LstOfPosn = new HashMap<Integer, Map<Integer, List<Integer>>>();
		
		Test3 t = new Test3();
		Map<Integer, Map<Integer, List<Integer>>> TermId_DocId_Posn_Lst = t.tokenize_one_doc("ap890101", "The car was in the car wash. And there is"
				+ " another car whose name is \"Car\" that's not being washed is being driven by tom's dad the tom I who doesn't own a car",
				termId_DocId_LstOfPosn);
		Map<Integer, Map<Integer, List<Integer>>> TermId_DocId_Posn_Lst2 = t.tokenize_one_doc("ap890101", "Tom's son's girlfriend Lily who doesn't"
				+ " have a car that is in the car wash is bored and wondered if she should own a car as well. "
				+ "She wouldn't name the car \"Car\" but something else",
				termId_DocId_LstOfPosn);
		
		System.out.println(TermId_DocId_Posn_Lst);
		System.out.println(TermId_DocId_Posn_Lst2);
		System.out.println(termId_DocId_LstOfPosn);
		
	}
	
	public Map<Integer, Map<Integer, List<Integer>>> tokenize_one_doc(String docno, String text, Map<Integer, Map<Integer, List<Integer>>> termId_DocId_LstOfPosn){

		// Since this is a new docno, increment the docId_
		docId_++;
		
		String pattern = "\\w+(\\.?\\w+)*";
		Matcher m = Pattern.compile(pattern).matcher(text);

		// 1) Create a class TermId_DocId_Posn, every time you read a document, you create an instance of List<TermId_DocId_Posn>,
		// and then after you read 1000 documents, you will end up with List<List<TermId_DocId_Posn>>. And then you have to merge
		// it into Map<TermId, Map<DocId, List<Posn>>>. Then you can print it out as your inverted index for 1000 documents
		// BAD APPROACH
		// List<TermId_DocId_Posn> termId_DocId_Posn_Lst = new ArrayList<TermId_DocId_Posn>();

		// 2) Every time you read a document, you create an instance of Map<TermId, Map<DocId, List<Posn>>>,
		// then when you read a new document, if you have seen the term before, put it to the same TermId map;
		// otherwise create a new TermId map.  And then you can directly print it out as your inverted index for 1000 documents
//		Map<Integer, Map<Integer, List<Integer>>> termId_DocId_LstOfPosn = new HashMap<Integer, Map<Integer, List<Integer>>>();
		Map<Integer, List<Integer>> docId_LstOfPosn; // = new HashMap<Integer, List<Integer>>();
		List<Integer> lstOfPosn; // = new ArrayList<Integer>();

		//		// Keep track a set of unique terms
		//		Set<String> terms = new HashSet<String>();
		//		Map<Integer, String> termId_term = new HashMap<Integer, String>();
		//		Map<String, Integer> term_termId = new HashMap<String, Integer>();

		//		Map<Integer, String> docId_docno = new HashMap<Integer, String>();
		//		Map<String, Integer> docno_docId = new HashMap<String, Integer>();


		// For every document

		int posn = -1;

		// The reason we have to keep this termId is because we might have seen this term
		// before. For example, "The car was in the car wash". "the" appears twice in
		// the sentence, so the second time we see it, we just want to use the existing
		// termId
		int termId = -100;
		String newTerm = "";
//		int docId = -100;
		while (m.find()) {

			// Find a next matching term by the group method and make it lower case
			newTerm = m.group().toLowerCase();

			//docId_++;
			posn++;
			if (term_termId_.containsKey(newTerm)){
				
				// If we have seen the term before
				// 1) Get its termId using the term_termId_
				// 2) Use its docId, as a key of docId_LstOfPosn to find its lstOfPosn, and add a new position (posn)
				termId = term_termId_.get(newTerm);
				
				// When you are reading the same document, you can just get its docId_
				// and append the posn to the position list
				if (termId_DocId_LstOfPosn.get(termId).containsKey(docId_)){
					termId_DocId_LstOfPosn.get(termId).get(docId_).add(posn);
				} else {
					// This happens when you are reading a new document and see a
					// term exists in the previous documents, in this case a new
					// Map<docId_, lstOfPosn> has to be created and be put into
					// the termId map - termId_DocId_LstOfPosn
					docId_LstOfPosn = new HashMap<Integer, List<Integer>>();
					lstOfPosn = new ArrayList<Integer>();
					lstOfPosn.add(posn);
					
					docId_LstOfPosn.put(docId_, lstOfPosn);
					
					//termId_DocId_LstOfPosn.put(termId, docId_LstOfPosn);	// this line is wrong
					termId_DocId_LstOfPosn.get(termId).put(docId_, lstOfPosn);
				}
//				docId_LstOfPosn.get(docId_).add(posn);
			} else {
				// If we haven't seen this term before
				// 1) Add it to the terms set
				// 2) Increment the termId_
				// 3) Put it to the termId_term_, term_termId_ maps
				// 4) Instantiate a lstOfPosn list to record the list of positions
				// 5) Add position posn to lstOfPosn
				// 6) docId_LstOfPosn put the docId_ and newly instantiated lstOfPosn
				// 7) termId_DocId_LstOfPosn put the new termId and docId_LstOfPosn
				terms_.add(newTerm);		

				termId_++;
				termId = termId_;

				termId_term_.put(termId, newTerm);
				term_termId_.put(newTerm, termId);
				
				docId_LstOfPosn = new HashMap<Integer, List<Integer>>();
				lstOfPosn = new ArrayList<Integer>();
				lstOfPosn.add(posn);
				
				docId_LstOfPosn.put(docId_, lstOfPosn);
				
				termId_DocId_LstOfPosn.put(termId, docId_LstOfPosn);								
			}
		}
		return termId_DocId_LstOfPosn;
	}


}
