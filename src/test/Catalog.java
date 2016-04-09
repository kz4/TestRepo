package test;

public class Catalog implements Comparable<Catalog> {

//	public Catalog (int lstName, int index, String term, int offset, int len) {
//		this.catName_ = lstName;
//		this.index_ = index;
//		this.term_ = term;
//		this.offset_ = offset;
//		this.len_ = len;
//	}
	public Catalog (int lstName, int index, String term) {
		this.catName_ = lstName;
		this.index_ = index;
		this.term_ = term;
	}
	
	public int catName_;
	public int index_;
	public String term_;
//	public int offset_;
//	public int len_;

	public int compareTo(Catalog entry) {
		return this.term_.compareToIgnoreCase(entry.term_);
	}
}
