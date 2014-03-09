package com.mikrasov.opencv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
	
	public static void saveImageToDisk(Mat source, File targetFile){

        Mat mat = source.clone();

        Bitmap bmpOut = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bmpOut);
        if (bmpOut != null){

            try {
            	FileOutputStream fout = new FileOutputStream(targetFile);
                BufferedOutputStream bos = new BufferedOutputStream(fout);
                bmpOut.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                bmpOut.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
        bmpOut.recycle();
    }

}
