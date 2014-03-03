package com.mikrasov.sensing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.util.Log;

import com.mikrasov.opencv.Util;
import com.mikrasov.opencv.blob.BlobDetector;
import com.mikrasov.opencv.blob.BlobList;

public class BeeDetector {

	private int threshold = 20;	
	private int minMass = 10, maxMass = 500, minHeight = 5, minWidth = 5, maxHeight =200, maxWidth = 200 ;
	
	private BackgroundSubtractorMOG2 background = new BackgroundSubtractorMOG2(4, 4, true);
	private Mat mRgb = new Mat();
	private Mat mFGMask = new Mat();
	

	
	public  void proccessFrame(Mat frame){
		//Imgproc.threshold(frame, frame, threshold, 255, Imgproc.THRESH_BINARY);

		mFGMask = new Mat(frame.size(), frame.type());
		
		//Imgproc.cvtColor(frame, mRgb, Imgproc.COLOR_GRAY2RGB); //the apply function will throw the above error if you don't feed it an RGB image
		background.apply(frame, frame); //apply() exports a gray image by definition

		//Core.bitwise_and(frame, mFGMask, frame);
        //Detect Blob
        BlobDetector detector = new BlobDetector( Util.convertToBitmap(frame) );
		//Bitmap annotated = blob.getBlob( convertToBitmap(frame) );
		
		BlobList blobList = detector.getBlobList().filterMass(minMass, maxMass).filterWidth(minWidth, maxWidth).filterHeight(minHeight, maxHeight);

		
		Log.v("Image","Found "+blobList.size()+" blobs:\n");
		
		
		
		(  detector.getAnnotationBoxed(blobList) ).copyTo(frame);
	}
	
	

	
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


}
