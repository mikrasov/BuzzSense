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

	private int threshold = 20;	
	private int minMass = 10, maxMass = 500, minHeight = 5, minWidth = 5, maxHeight =200, maxWidth = 200 ;
	
	private BackgroundSubtractorMOG2 background = new BackgroundSubtractorMOG2(50, 10, true);
	private Mat result = new Mat();
	private Mat bgMask = new Mat();
	

	
	public  void proccessFrame(Mat frame){
		//Imgproc.threshold(frame, frame, threshold, 255, Imgproc.THRESH_BINARY);

		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
		Imgproc.equalizeHist(frame, frame);
		
		bgMask = new Mat(frame.size(), frame.type());
		result = new Mat(frame.size(), frame.type());
		
		result.setTo(new Scalar(255,255,255));
		
		//Imgproc.cvtColor(frame, mRgb, Imgproc.COLOR_GRAY2RGB); //the apply function will throw the above error if you don't feed it an RGB image
		background.apply(frame, bgMask, 0); //apply() exports a gray image by definition

		//Core.bitwise_and(frame, mFGMask, frame);
        //Detect Blob
       
		//Bitmap annotated = blob.getBlob( convertToBitmap(frame) );
		
		

		//Log.v("Image","Found "+blobList.size()+" blobs:\n");
		
		
		//Log.v("IMAGE","SIZE frame:"+frame.size()+" Mask: "+bgMask.size());
		//detector.getAnnotationBoxed(mFGMask, blobList) ;
		
		BlobDetector detector = new BlobDetector( Util.convertToBitmap(result) );
		BlobList blobList = detector.getBlobList();//.filterMass(minMass, maxMass).filterWidth(minWidth, maxWidth).filterHeight(minHeight, maxHeight);
		
		
		frame.copyTo(result, bgMask);
		
		//Core.subtract(result, frame, result, bgMask);
		result.copyTo(frame);
		
		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2RGB);
		
		
		detector.getAnnotationBoxed(frame, blobList);
		
	}
	
	

	
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


}
