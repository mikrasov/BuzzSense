package com.mikrasov.opencv.blobslow;

import java.util.ArrayList;

public class BlobList extends ArrayList<Blob> {

	private static final long serialVersionUID = 2733856820140365699L;

	
	public BlobList filterMass(int min, int max){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			if(b.mass <= max && b.mass >= min )
				filtered.add(b);
		}
		return filtered;
	}
	
	public BlobList filterWidth(int min, int max){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			int width = b.xMax - b.xMin;
			
			if(width <= max && width >= min )
				filtered.add(b);
		}
		return filtered;
	}
	
	public BlobList filterHeight(int min, int max){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			int height = b.yMax - b.yMin;
			
			if(height <= max && height >= min )
				filtered.add(b);
		}
		return filtered;
	}
	
	
}
