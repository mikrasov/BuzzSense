package com.mikrasov.opencv.blob;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import com.mikrasov.opencv.Util;

import android.graphics.Bitmap;
import android.graphics.Color;

/*
 * CV Blob Library
 * https://code.google.com/p/cvblob-for-android/
 * GPL Licence
 */

public class BlobDetector {

	private int[] labelBuffer;
	private int[][] labelBufferCoordinates;
	private int[] labelTable;
	private int[] xMinTable;
	private int[] xMaxTable;
	private int[] yMinTable;
	private int[] yMaxTable;
	private int[] massTable;
	private BlobList blobList ;
	private Bitmap source;
	
	public  BlobList getBlobList(){
		return blobList;
	}
	

	
	public BlobDetector(Bitmap bitmap) {
		source = bitmap.copy(Bitmap.Config.ARGB_8888, true);;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		labelBuffer = new int[width * height];
		labelBufferCoordinates = new int[width][height];
		// The maximum number of blobs is given by an image filled with equally spaced single pixel
		// blobs. For images with less blobs, memory will be wasted, but this approach is simpler and
		// probably quicker than dynamically resizing arrays
		int tableSize = width * height / 4;

		labelTable = new int[tableSize];
		xMinTable = new int[tableSize];
		xMaxTable = new int[tableSize];
		yMinTable = new int[tableSize];
		yMaxTable = new int[tableSize];
		massTable = new int[tableSize];
	
		// This is the neighbouring pixel pattern. For position X, A, B, C & D are checked
		// A B C
		// D X

		int srcPtr = 0;
		int aPtr = -width - 1;
		int bPtr = -width;
		int cPtr = -width + 1;
		int dPtr = -1;

		int label = 1;

		int minBlobMass = 0;
		int maxBlobMass = -1;
		
		Blob blobMayor = null;
		blobList = new BlobList();
		
		//First pass
		for (int y = 0; y< bitmap.getHeight() ;y++) {
			for (int x = 0;x<bitmap.getWidth();x++ ) {
				// if data[row][col] is not Background
				if ( bitmap.getPixel(x, y) == Color.BLACK ) {
					labelBuffer[srcPtr] = 0;
					labelBufferCoordinates[x][y] = 0;
					// Find label for neighbours (0 if out of range)
					int aLabel = (x > 0 && y > 0)			? labelTable[labelBuffer[aPtr]] : 0;
					int bLabel = (y > 0)						? labelTable[labelBuffer[bPtr]] : 0;
					int cLabel = (x < width-1 && y > 0)	? labelTable[labelBuffer[cPtr]] : 0;
					int dLabel = (x > 0)						? labelTable[labelBuffer[dPtr]] : 0;

					// Look for label with least value
					int min = Integer.MAX_VALUE;
					if (aLabel != 0 && aLabel < min) min = aLabel;
					if (bLabel != 0 && bLabel < min) min = bLabel;
					if (cLabel != 0 && cLabel < min) min = cLabel;
					if (dLabel != 0 && dLabel < min) min = dLabel;

					// If no neighbours in foreground
					if (min == Integer.MAX_VALUE)
					{
						labelBuffer[srcPtr] = label;
						labelBufferCoordinates[x][y] = label;
						labelTable[label] = label;	

						// Initialise min/max x,y for label
						yMinTable[label] = y;
						yMaxTable[label] = y;
						xMinTable[label] = x;
						xMaxTable[label] = x;	
						massTable[label] = 1;

						label ++;
					}

					// Neighbour found
					else
					{
						// Label pixel with lowest label from neighbours	
						labelBuffer[srcPtr] = min;
						labelBufferCoordinates[x][y] = min;
							// Update min/max x,y for label
							yMaxTable[min] = y;
							massTable[min]++;
							if (x < xMinTable[min]) xMinTable[min] = x;
							if (x > xMaxTable[min]) xMaxTable[min] = x;

							if (aLabel != 0) labelTable[aLabel] = min;
							if (bLabel != 0) labelTable[bLabel] = min;
							if (cLabel != 0) labelTable[cLabel] = min;
							if (dLabel != 0) labelTable[dLabel] = min;
						}
					}

					srcPtr ++;
					aPtr ++;
					bPtr ++;
					cPtr ++;
					dPtr ++;
				}
			}

			// Iterate through labels pushing min/max x,y values towards minimum label
			if (blobList == null) blobList = new BlobList();

			for (int i=label-1 ; i>0 ; i--)
			{
				if (labelTable[i] != i)
				{
					if (xMaxTable[i] > xMaxTable[labelTable[i]]) xMaxTable[labelTable[i]] = xMaxTable[i];
					if (xMinTable[i] < xMinTable[labelTable[i]]) xMinTable[labelTable[i]] = xMinTable[i];
					if (yMaxTable[i] > yMaxTable[labelTable[i]]) yMaxTable[labelTable[i]] = yMaxTable[i];
					if (yMinTable[i] < yMinTable[labelTable[i]]) yMinTable[labelTable[i]] = yMinTable[i];
					massTable[labelTable[i]] += massTable[i];

					int l = i;
					while (l != labelTable[l]) l = labelTable[l];
					labelTable[i] = l;
				}
				else
				{
					// Ignore blobs that butt against corners
					if (i == labelBuffer[0]) continue;									// Top Left
					if (i == labelBuffer[width]) continue;								// Top Right
					if (i == labelBuffer[(width*height) - width + 1]) continue;	// Bottom Left
					if (i == labelBuffer[(width*height) - 1]) continue;			// Bottom Right

					if (massTable[i] >= minBlobMass && (massTable[i] <= maxBlobMass || maxBlobMass == -1))
					{
						Blob blob = new Blob(xMinTable[i], xMaxTable[i], yMinTable[i], yMaxTable[i], massTable[i]);
						if ( blobMayor == null || blob.mass >= blobMayor.mass ) 
							blobMayor = blob;
						blobList.add(blob);
					}
				}
			}
	}
	
	public Mat getAnnotationShaded(){
		int maximoBlob = 0;
		int posicion = 0;
		for (int i = 0;i<massTable.length;i++) {
			if ( massTable[i] > maximoBlob ) {
				posicion = i;
				maximoBlob = massTable[i];
			}
				
		}
		
		Bitmap annotated = source.copy(Bitmap.Config.ARGB_8888, true);
		for (int y = 0; y< source.getHeight() ;y++) {
			for (int x = 0;x<source.getWidth();x++ ) {
				if ( labelTable[labelBufferCoordinates[x][y]] == posicion ) {	
					annotated.setPixel(x, y, Color.BLACK);	
				}   else  if ( labelTable[labelBufferCoordinates[x][y]] != 0) {
					annotated.setPixel(x, y, Color.GREEN);
				} else {
					annotated.setPixel(x, y, Color.WHITE);
				}
			}
		}
		return Util.convertToMat(annotated);
	}
	
	
	public Mat getAnnotationBoxed(){
		return getAnnotationBoxed(Util.convertToMat(source), blobList);
	}
	
	public Mat getAnnotationBoxed(Mat image){
		return getAnnotationBoxed(image, blobList);
	}
	
	public Mat getAnnotationBoxed(Mat image, BlobList list){
		for (Blob b : list)  {
			Core.rectangle(image, new Point(b.xMin, b.yMin), new Point(b.xMax, b.yMax), new Scalar(0,155,00), 5);
		}
		return image;
	}
}
