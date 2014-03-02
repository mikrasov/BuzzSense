package com.mikrasov.sensing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.util.Log;

import com.mikrasov.blobdetect.Blob;
import com.mikrasov.blobdetect.BlobDetector;
import com.mikrasov.blobdetect.BlobList;
import com.mikrasov.blobdetect.Util;

public class BeeDetector {

	private int threshold = 20;	
	private int minMass = 25, maxMass = 500;
	
	public  void proccessFrame(Mat frame){
		Imgproc.threshold(frame, frame, threshold, 255, Imgproc.THRESH_BINARY);

        //Detect Blob
        BlobDetector detector = new BlobDetector( Util.convertToBitmap(frame) );
		//Bitmap annotated = blob.getBlob( convertToBitmap(frame) );
		
		BlobList blobList = detector.getBlobList().filter(minMass, maxMass);

		
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
