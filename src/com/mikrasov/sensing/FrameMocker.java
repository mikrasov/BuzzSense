package com.mikrasov.sensing;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.os.Environment;
import android.util.Log;

public class FrameMocker {

	private File[] sampleImages;
    private int sampleImageNum = -1 ;
    
    
	public FrameMocker() {
		sampleImages = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/media/samples/").listFiles();
	}
	
    public void getNextFrame(Mat frameToReplace) {
		
    	//Error Case
    	if(sampleImages.length <= 0){
    		Log.e("Image", "No sample images dound");
    		return;
    	}
    	
    	if(sampleImageNum +1 < sampleImages.length)sampleImageNum++;    	
    	else 						     		sampleImageNum = 0;

    	String filePath = sampleImages[sampleImageNum].getAbsolutePath();
		Mat	img = Highgui.imread(filePath);
		img.convertTo(img, frameToReplace.type());
		Imgproc.resize(img, frameToReplace, frameToReplace.size());
			
		Log.d("FILE","Loading file "+filePath);
    }

}
