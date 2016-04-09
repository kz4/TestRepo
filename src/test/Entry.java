package test;

import java.util.Iterator;

public class Entry implements Comparable<Entry>
{
//	private Term_Offset_Len value;
//	private Iterator<Term_Offset_Len> it;
//
//	public Entry (Term_Offset_Len value, Iterator<Term_Offset_Len> it) {
//		this.value = value;
//		this.it = it;
//	}
//
//	public Term_Offset_Len getValue() {
//		return this.value;
//	}
//
//	public boolean readNext() {
//		if (it.hasNext()) {
//			value = it.next();
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	public Entry (int lstName, int index, String term) {
		this.lstName_ = lstName;
		this.index_ = index;
		this.term_ = term;
	}
	
	public int lstName_;
	public int index_;
	public String term_;

	public int compareTo(Entry entry) {
//		return this.value.term_.compareToIgnoreCase(entry.value.term_);
		return this.term_.compareToIgnoreCase(entry.term_);
	}
} 