package com.mikrasov.blobdetect;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import android.graphics.Bitmap;

public class Util {

	
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
	

}
