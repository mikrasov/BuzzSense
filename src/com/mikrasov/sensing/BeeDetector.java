package com.mikrasov.sensing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

import android.os.Environment;
import android.util.Log;

import com.mikrasov.opencv.Util;
import com.mikrasov.opencv.blob.Blob;
import com.mikrasov.opencv.blob.BlobDetector;
import com.mikrasov.opencv.blob.BlobList;

public class BeeDetector {

	private int THRESHOLD = 40;
	private int BG_HISTORY_LENGTH = 50;
	private double BG_LEARNING_RATE = 0.01;
	private float BG_THRESHOLD = 10;
	private boolean BG_SUBTRACT_SHADOW = false;
	
	private int minMass = 50, maxMass = 5000, minHeight = 10, minWidth = 10, maxHeight =900, maxWidth = 900 ;
	
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

		original.copyTo(source);

		//Normalize
		Imgproc.cvtColor(original, original, Imgproc.COLOR_RGB2GRAY);
		Imgproc.equalizeHist(original, original);
		
		
		bgMask = new Mat(original.size(), original.type());		
		intermidiate = new Mat(original.size(), original.type());
		
		intermidiate.setTo(new Scalar(255,255,255));
		
		background.apply(original, bgMask, BG_LEARNING_RATE); //apply() exports a gray image by definition

		
		original.copyTo(intermidiate, bgMask);
		Imgproc.threshold(intermidiate, intermidiate, THRESHOLD, 255, Imgproc.THRESH_BINARY);
		//Imgproc.adaptiveThreshold(intermidiate, intermidiate,255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 37, 77);
		
		//intermidiate.copyTo(source);
		
		Imgproc.cvtColor(intermidiate, intermidiate, Imgproc.COLOR_GRAY2RGB);
		
		final BlobDetector detector = new BlobDetector( Util.convertToBitmap(intermidiate) );
		final BlobList blobList = detector.getBlobList().filterMass(minMass, maxMass).filterWidth(minWidth, maxWidth).filterHeight(minHeight, maxHeight);
		
		source.copyTo(original);
		
		//count
		int numBees = 0;
		for(Blob b: blobList)
			numBees += count(b);
		
		
		//Write to Log
		try {
			log.write(filename+","+numBees+"\n");
			log.flush();
		} catch (IOException e) {}
		
		//annotate
		//detector.getAnnotationShaded(original);
		getAnnotationBoxed(original, blobList);
		getAnnotationText(original, blobList);
		Core.putText(original, numBees+"", new Point(100,100), Core.FONT_HERSHEY_COMPLEX, 3, new Scalar(0,255,0) );	
		
		
		//Save files
		File path = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/media/processed/");
        path.mkdirs();
        //Util.saveImageToDisk(source,  new File(path, filename+"_org.jpg"));
        Util.saveImageToDisk(original,  new File(path, filename+"_ano.jpg"));
	}
	
	public Mat getAnnotationBoxed(Mat image, BlobList list){
		for (Blob b : list)  {
			Core.rectangle(image, new Point(b.xMin, b.yMin), new Point(b.xMax, b.yMax), new Scalar(255,0,255), 2);
		}
		return image;
	}
	
	public Mat getAnnotationText(Mat image, BlobList list){
		
		for (Blob b : list)  {
			Core.putText(image, b.mass +"", new Point(b.xMax,b.yMax), Core.FONT_HERSHEY_PLAIN, 1.5, new Scalar(255,0,255) );		
			Core.putText(image, count(b)+"", new Point(b.xMax,b.yMin), Core.FONT_HERSHEY_PLAIN, 1.5, new Scalar(0,255,0) );		
		}
		return image;
	}
	

	private int count(Blob b){
		return (int) Math.round(b.mass/180.0);
	}

}
