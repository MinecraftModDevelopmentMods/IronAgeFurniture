package com.mcmoddev.ironagefurniture.util;

public class Coordinates {
	public double X;
	public double Y;
	public double Z;
	
	public Coordinates() {}
	
	public Coordinates(double x, double y, double z) {
		X = x;
		Y = y;
		Z = z;
	}
	
	public boolean Match(double x, double y, double z) {
		if (X==x && Y ==y && Z == z)
			return true;
		
		return false;
	}
}
