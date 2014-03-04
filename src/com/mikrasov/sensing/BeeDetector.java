package com.mikrasov.sensing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.util.Log;

import com.mikrasov.opencv.Util;
import com.mikrasov.opencv.blob.BlobDetector;
import com.mikrasov.opencv.blob.BlobList;

public class BeeDetector {

	private int THRESHOLD = 40;
	private int BG_HISTORY_LENGTH = 50;
	private double BG_LEARNING_RATE = 0.01;
	private float BG_THRESHOLD = 10;
	private boolean BG_SUBTRACT_SHADOW = false;
	
	private int minMass = 10, maxMass = 5000, minHeight = 7, minWidth = 7, maxHeight =400, maxWidth = 400 ;
	
	private BackgroundSubtractorMOG2 background = new BackgroundSubtractorMOG2(BG_HISTORY_LENGTH, BG_THRESHOLD, BG_SUBTRACT_SHADOW);
	private Mat source = new Mat();
	private Mat intermidiate = new Mat();
	private Mat bgMask = new Mat();
	

	
	public  void proccessFrame(Mat original){

		original.copyTo(source);
		
		Imgproc.cvtColor(original, original, Imgproc.COLOR_RGB2GRAY);
		Imgproc.equalizeHist(original, original);
		
		bgMask = new Mat(original.size(), original.type());		
		intermidiate = new Mat(original.size(), original.type());
		
		intermidiate.setTo(new Scalar(255,255,255));
		
		background.apply(original, bgMask, BG_LEARNING_RATE); //apply() exports a gray image by definition

		
		original.copyTo(intermidiate, bgMask);
		Imgproc.threshold(intermidiate, intermidiate, THRESHOLD, 255, Imgproc.THRESH_BINARY);
		Imgproc.cvtColor(intermidiate, intermidiate, Imgproc.COLOR_GRAY2RGB);
		
		BlobDetector detector = new BlobDetector( Util.convertToBitmap(intermidiate) );
		BlobList blobList = detector.getBlobList().filterMass(minMass, maxMass).filterWidth(minWidth, maxWidth).filterHeight(minHeight, maxHeight);
		
		source.copyTo(original);
		
		detector.getAnnotationBoxed(original, blobList);

	}
	
	

	
	public int getThreshold() {
		return THRESHOLD;
	}

	public void setThreshold(int threshold) {
		this.THRESHOLD = threshold;
	}


}
