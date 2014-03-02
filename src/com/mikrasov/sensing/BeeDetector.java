package com.mikrasov.sensing;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

public class BeeDetector {

	private int threshold = 20;	
	
	public  void proccessFrame(Mat frame){
		Imgproc.threshold(frame, frame, threshold, 255, Imgproc.THRESH_BINARY);

        //Detect Blob
        BlobDetection blob = new BlobDetection( convertToBitmap(frame) );
		Bitmap annotated = blob.getBlob( convertToBitmap(frame) );
		
		Log.v("Image","Found "+blob.blobList.size()+" blobs:\n");
		for (BlobDetection.Blob blobies : blob.blobList)  {
			Log.v("Image", blobies.toString());
		}
		
		( convertToMat(annotated) ).copyTo(frame);
	}
	
	public static Mat convertToMat(Bitmap bitmap){
        Mat result = new Mat ( bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8U, new Scalar(4));
        Bitmap myBitmap32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, result);
        return result;
	}
	
	public static Bitmap convertToBitmap(Mat frame){
		Bitmap bitmap = Bitmap.createBitmap(frame.cols(), frame.rows(),Bitmap.Config.ARGB_8888);;
        Utils.matToBitmap(frame, bitmap);
        return bitmap;
	}
	
	
	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


}
