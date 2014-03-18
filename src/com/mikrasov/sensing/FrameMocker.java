package com.mikrasov.sensing;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.os.Environment;
import android.util.Log;

public class FrameMocker extends Thread {

	private File[] sampleImages;
    private int sampleImageNum = -1 ;
    private static int BUFFER_SIZE = 10;
    private LinkedList<Mat> buffer;
    
	public FrameMocker() {
		sampleImages = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/media/samples/").listFiles();
		Arrays.sort(sampleImages);
		buffer = new LinkedList<Mat>();
	}
	
	
    public File getNextFrame(Mat frameToReplace) {
    	if(sampleImages.length <= 0){
    		Log.e("Image", "No sample images found");
    		return null;
    	}
    	while(buffer.size() < 1){
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
    	}
    	
    	Mat img = buffer.pop();
    	img.convertTo(img, frameToReplace.type());
		Imgproc.resize(img, frameToReplace, frameToReplace.size());
		
		
		return sampleImages[sampleImageNum];
    }

	@Override
	public void run() {
		while(true){
			if(buffer.size() < BUFFER_SIZE){
				//Error Case
		    	if(sampleImages.length <= 0){
		    		Log.e("Image", "No sample images found");
		    		return;
		    	}
		    	
		    	if(sampleImageNum +1 < sampleImages.length)sampleImageNum++;    	
		    	else 						     		sampleImageNum = 0;
		    	
		    	String filePath = sampleImages[sampleImageNum].getAbsolutePath();
		    	Log.d("FILE","Buffering file "+filePath);
				buffer.add(Highgui.imread(filePath));
			}
			else{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {				}
			}
		}
		
	}

}
