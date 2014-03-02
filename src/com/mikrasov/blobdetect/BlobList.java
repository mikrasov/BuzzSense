package com.mikrasov.blobdetect;

import java.util.ArrayList;

public class BlobList extends ArrayList<Blob> {

	private static final long serialVersionUID = 2733856820140365699L;

	
	public BlobList filter(int minMass, int maxMass){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			if(b.mass < maxMass && b.mass > minMass )
				filtered.add(b);
		}
		return filtered;
	}
}
