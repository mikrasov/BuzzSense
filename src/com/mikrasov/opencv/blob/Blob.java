package com.mikrasov.opencv.blob;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Blob {

	private final MatOfPoint contour;
	private Rect boundingBox;
	private double area = -1;
	
	public Blob(MatOfPoint contour) {
		this.contour = contour;
	}
	
	public double getArea(){
		if(area < 0)
			area = Imgproc.contourArea(contour);
		return area;
	}
	
	public Point getMin(){
		return new Point(getMinX(),getMinY());
	}
	public Point getMax(){
		return new Point(getMaxX(),getMaxY());
	}
	
	public int getMinX(){
		return getBoundingBox().x;
	}
	
	public int getMinY(){
		return getBoundingBox().y;
	}
	
	public int getMaxX(){
		return getBoundingBox().x+getBoundingBox().width;
	}
	
	public int getMaxY(){
		return getBoundingBox().y+getBoundingBox().height;
	}

	public int getWidth(){
		return getBoundingBox().width;
	}
	
	public int getHeight(){
		return getBoundingBox().height;
	}
	public MatOfPoint getContour(){
		return contour;
	}
	
	private Rect getBoundingBox(){
		if(boundingBox == null) 
			boundingBox = Imgproc.boundingRect(contour);
		return boundingBox;
	}
	
	public void annotateBounds(Mat image, Scalar color, int width){
		Core.rectangle(image, getMin(), getMax(), color, width); 
	}
}
