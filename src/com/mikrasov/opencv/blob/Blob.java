package com.mikrasov.opencv.blob;

public class Blob {

	public final int xMin;
	public final int xMax;
	public final int yMin;
	public final int yMax;
	public final int mass;

	public Blob(int xMin, int xMax, int yMin, int yMax, int mass){
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.mass = mass;
	}

	public Blob(Blob blob){
		this.xMin = blob.xMin;
		this.xMax = blob.xMax;
		this.yMin = blob.yMin;
		this.yMax = blob.yMax;
		this.mass = blob.mass;
	}
	
	public String toString() {
		return String.format("X: %4d -> %4d, Y: %4d -> %4d, mass: %6d", xMin, xMax, yMin, yMax, mass);
	}

}
