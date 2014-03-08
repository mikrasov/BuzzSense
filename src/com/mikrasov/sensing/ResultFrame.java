package com.mikrasov.sensing;

import org.opencv.core.Mat;

public abstract class ResultFrame {

	public Mat getOriginal(boolean annotated) {
		if(annotated) 
			return annotate(getOriginal());
		else
			return getOriginal();
	}
	public Mat getProccessed(boolean annotated) {
		if(annotated) 
			return annotate(getProccessed());
		else
			return getProccessed();
	}
	
	protected abstract Mat getOriginal();
	protected abstract Mat getProccessed();
	protected abstract Mat annotate(Mat imageToAnnotate);
}
