package org.frc2018.math;

import java.util.Vector;

public class Vector2 {

	public double x = 0.0, y = 0.0;
	
	/**
	 * Creates a Vector2 with the given x and y magnitudes.
	 * 
	 * @param x The x magnitude of the new vector
	 * @param y The y magnitude of the new vector
	 */
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Adds a list of vectors to this vector.
	 * 
	 * @param vectors The list of vectors to add to this vector
	 */
	public void add(Vector2... vectors) {
		for(Vector2 vector : vectors) {
			x += vector.x;
			y += vector.y;
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return a + b
	 */
	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}

	/**
	 * 
	 * @param a first vector
	 * @param b second vector
	 * @return returns a - b
	 */
	public static Vector2 subtract(Vector2 a, Vector2 b) {
		return new Vector2(a.x - b.x, a.y - b.y);
	}

	public static double dot(Vector2 a, Vector2 b) {
		return a.x * b.x + a.y * b.y;
	}
	
	/**
	 * Multiplies this vector by a scalar.
	 * 
	 * @param coef The scalar to multiply this vector by
	 */
	public void multiply(double coef) {
		x *= coef;
		y *= coef;
	}

	/*
	public double dot(Vector2 other) {
		return y * other.y + x * other.x;
	}
	*/
	
	/**
	 * Gets the magnitude of this vector.
	 * 
	 * @return The magnitude of this vector
	 */
	public double getMagnitude() {
		return Math.sqrt(x*x + y*y);
	}
	
	/** 
	 * Gets the heading of this vector in compass degrees (0 to 360).
	 * 
	 * @return The heading of this vector in compass degrees from 0 to 360
	 */
	public double getHeading() {
		double rawHeading = 450.0 - Math.toDegrees(Math.atan2(y, x));
		while(rawHeading < 0.0) {
			rawHeading += 360.0;
		}
		while(rawHeading > 360.0) {
			rawHeading -= 360.0;
		}
		return rawHeading;
	}
	
	public String toString() {
		return "[" + x + " " + y + "]";
	}
	
	/**
	 * Gets the angle between two vectors from from to to in degrees from -180 to 180.
	 * 
	 * @param from The vector to measure the angle from
	 * @param to The vector to measure the angle to
	 * 
	 * @return The angle between the two vectors from -180 to 180
	 */
	public static double angleBetween(Vector2 from, Vector2 to) {
		double rawAngleDelta = from.getHeading() - to.getHeading();
		double adjustedAngleDelta = -360.0 - rawAngleDelta;
		while(adjustedAngleDelta > 180.0) {
			adjustedAngleDelta -= 360.0;
		}
		while(adjustedAngleDelta < -180.0) {
			adjustedAngleDelta += 360.0;
		}
		return adjustedAngleDelta;
	}
	
	/**
	 * Creates a unit vector with the specified heading.
	 * 
	 * @param heading The heading of the desired unit vector
	 * 
	 * @return A unit vector with the desired heading
	 */
	public static Vector2 representHeadingWithUnitVector(double heading) {
		return new Vector2(Math.cos(Math.toRadians(450-heading)), Math.sin(Math.toRadians(450-heading)));
	}
	
	/**
	 * Creates a copy of the provided vector.
	 * 
	 * @param original The original vector
	 * 
	 * @return The new vector
	 */
	public static Vector2 copyVector(Vector2 original) {
		return new Vector2(original.x, original.y);
	}
	
	/**
	 * Gets the distance between two vectors.
	 * 
	 * @param from The first vector
	 * @param to The second vector
	 * 
	 * @return The distance between the two vectors
	 */
	public static double distanceBetween(Vector2 from, Vector2 to) {
		return new Vector2(from.y-to.y, from.x-to.x).getMagnitude();
	}
	
	/**
	 * A vector with 0 magnitude.
	 */
	public static Vector2 zero = new Vector2(0.0, 0.0);
	
}