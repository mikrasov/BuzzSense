package com.mikrasov.sensing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.os.Environment;

import com.mikrasov.opencv.Util;
import com.mikrasov.opencv.blob.Blob;
import com.mikrasov.opencv.blob.BlobList;

public class BeeDetector {

	private int BG_HISTORY_LENGTH = 10;
	private double BG_LEARNING_RATE = 0.0005;
	private float BG_THRESHOLD = 100;
	private boolean BG_SUBTRACT_SHADOW = false;
	private int BINARY_THRESHOLD = 80;
	private double AVERAGE_BEE_AREA = 200;
	
	private int MIN_AREA   = 50,	MAX_AREA = -1;
	private int MIN_HEIGHT = 12,	MIN_WIDTH = 12;
	private int MAX_HEIGHT = 900,	MAX_WIDTH = 900;
	
	private long totalproccessingTime = 0;
	private long numFrames = 0;
	
	private BackgroundSubtractorMOG2 background = new BackgroundSubtractorMOG2(BG_HISTORY_LENGTH, BG_THRESHOLD, BG_SUBTRACT_SHADOW);
	
	private Mat intermidiate = new Mat();
	private Mat bgMask = new Mat();
	private Mat source = new Mat();
	
	private BufferedWriter log;
	public BeeDetector(){
		try {
			log = new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/media/processed/out.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void proccessFrame(Mat original){
		proccessFrame(original, System.currentTimeMillis()+"");
	}
	
	public void proccessFrame(Mat original, String filename){
		
		//Set Save path
		//File path = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/media/processed/");
        //path.mkdirs();
        
        
		original.copyTo(source);

		//Util.saveImageToDisk(original,  new File(path, filename+"_0_original.jpg"));

		//START COUNTING time
		long startTime = System.currentTimeMillis();
		
		//Normalize
		Imgproc.cvtColor(original, original, Imgproc.COLOR_RGB2GRAY);		
		Imgproc.equalizeHist(original, original);
		
		bgMask = new Mat(original.size(), original.type());		
		intermidiate = new Mat(original.size(), original.type()).setTo(new Scalar(255,255,255));
		
		//Background Subtraction
		background.apply(original, bgMask, BG_LEARNING_RATE); 

		//Apply BG Mask
		original.copyTo(intermidiate, bgMask);
		
		
		//Applies Binary Thresholding
		Imgproc.threshold(intermidiate, intermidiate, BINARY_THRESHOLD, 255, Imgproc.THRESH_BINARY);
		//Imgproc.adaptiveThreshold(intermidiate, intermidiate,255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 37, 77);
		
		BlobList blobs = new BlobList(intermidiate).filterWidth(MIN_WIDTH, MAX_WIDTH).filterHeight(MIN_HEIGHT, MAX_HEIGHT).filterArea(MIN_AREA, MAX_AREA);
		
		int numBees = 0;
		for(Blob b: blobs)
			numBees += count(b);
		
		//Calc runtime and average frame rate
		long endTime = System.currentTimeMillis();
		long proccessingTime = endTime - startTime;
		totalproccessingTime += proccessingTime;
		numFrames++;
		double frameRate = numFrames/((double)totalproccessingTime/1000);
		
		//Write to Log
		/*
		try {
			log.write(filename+","+numBees+","+proccessingTime+"\n");
			log.flush();
		} catch (IOException e) {}
		*/
		
		source.copyTo(original);

		//annotate
		blobs.annotateContours(original, new Scalar(255,0,0));
		for (Blob b : blobs)  {
			//Core.putText(original, b.getArea() +"", new Point(b.getMaxX(),b.getMaxY()), Core.FONT_HERSHEY_PLAIN, 1.5, new Scalar(255,255,0) );		
			Core.putText(original, count(b)+"", new Point(b.getMaxX(),b.getMinY()), Core.FONT_HERSHEY_PLAIN, 1.5, new Scalar(0,255,0) );		
			
			//Core.putText(original, "w:"+b.getWidth(), new Point(b.getMaxX(),b.getMinY()), Core.FONT_HERSHEY_PLAIN, 1.1, new Scalar(0,255,255) );	
			//Core.putText(original, "h:"+b.getHeight(), new Point(b.getMaxX(),b.getMaxY()), Core.FONT_HERSHEY_PLAIN, 1.1, new Scalar(255,255,0) );	
		}
		
		Core.putText(original, numBees+"", new Point(50,75), Core.FONT_HERSHEY_COMPLEX, 3, new Scalar(0,255,0) );	
		Core.putText(original, String.format("%.1f", frameRate)+"fps", new Point(750,50), Core.FONT_HERSHEY_COMPLEX, 1.8, new Scalar(255,255,0) );	
		
		//Util.saveImageToDisk(original,  new File(path, filename+"_6_annotated.jpg"));
	
	}
	

	private int count(Blob b){
		int count = (int) Math.round(b.getArea()/AVERAGE_BEE_AREA);
		if(count < 1) return 1;
		else return count;
	}
}
