package com.mikrasov.opencv.blob;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class BlobList extends LinkedList<Blob> {

	private static final long serialVersionUID = 2733856820140365699L;

	public BlobList(){
		super();
	}
	
	public BlobList(Mat image){
		super();
		this.addAll( process(image) );
	}
	
	public BlobList(List<MatOfPoint> contours){
		super();
		for(MatOfPoint c: contours)
			add(new Blob(c));
	}
	
	public BlobList filterArea(int min, int max){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			if((max < 0 || b.getArea() <= max) && 
			   (min < 0 || b.getArea() >= min) ){
				filtered.add(b);
			}
		}
		return filtered;
	}
	
	public BlobList filterWidth(int min, int max){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			int width = b.getWidth();
			
			if( (max < 0 || width <= max) && 
				(min < 0 || width >= min) ){
				filtered.add(b);
			}
		}
		return filtered;
	}
	
	public BlobList filterHeight(int min, int max){
		BlobList filtered = new BlobList();
		for(Blob b: this ){
			int height = b.getHeight();
			
			if( (max < 0 || height <= max ) &&
				(min < 0 || height >= min )){
				filtered.add(b);
			}
		}
		return filtered;
	}
	
	public List<MatOfPoint> getContourList(){
		List<MatOfPoint> contours = new LinkedList<MatOfPoint>();
		for(Blob b: this)
			contours.add(b.getContour());
		return contours;
	}
	
	public void annotateContours(Mat image, Scalar color){
		Imgproc.drawContours(image, getContourList(), -1, color);
	}
	
	public static BlobList process(Mat image){
		List<MatOfPoint> contours= new LinkedList<MatOfPoint>();
		Mat heiarchy = new Mat(new Size(320,240),CvType.CV_8UC1,new Scalar(0));
        Imgproc.findContours(image, contours, heiarchy, Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
		
        return new BlobList(contours);
	}
}
