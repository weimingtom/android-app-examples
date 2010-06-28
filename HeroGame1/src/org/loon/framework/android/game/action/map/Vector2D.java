package org.loon.framework.android.game.action.map;

import java.io.Serializable;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Vector2D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1844534518528011982L;

	private double x, y;

	public Vector2D(double value) {
		this(value, value);
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D vector2D) {
		this.x = vector2D.x;
		this.y = vector2D.y;
	}

	public void move(Vector2D vector2D) {
		this.x += vector2D.x;
		this.y += vector2D.y;
	}

	public void move_multiples(int direction, int multiples) {
		if (multiples <= 0) {
			multiples = 1;
		}
		Vector2D v = Field2D.getDirection(direction);
		move(v.x() * multiples, v.y() * multiples);
	}

	public void move(double x, double y) {
		this.x += x;
		this.y += y;
	}

	public double[] getCoords() {
		return (new double[] { x, y });
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void rotate90() {
		setLocation(y, -x);
	}

	public boolean equals(Object o) {
		if (o instanceof Vector2D) {
			Vector2D p = (Vector2D) o;
			return p.x == x && p.y == y;
		}
		return false;
	}

	public int hashCode() {
		return (int) (x + y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int x() {
		return (int) x;
	}

	public int y() {
		return (int) y;
	}

	public Object clone() {
		return new Vector2D(x, y);
	}

	public Vector2D add(Vector2D other) {
		double x = this.x + other.x;
		double y = this.y + other.y;
		return new Vector2D(x, y);
	}

	public Vector2D subtract(Vector2D other) {
		double x = this.x - other.x;
		double y = this.y - other.y;
		return new Vector2D(x, y);
	}

	public Vector2D multiply(double value) {
		return new Vector2D(value * x, value * y);
	}

	public double dotProduct(Vector2D other) {
		return other.x * x + other.y * y;
	}

	public Vector2D normalize() {
		double magnitude = Math.sqrt(dotProduct(this));
		return new Vector2D(x / magnitude, y / magnitude);
	}

	public double level() {
		return Math.sqrt(dotProduct(this));
	}

	public Vector2D modulate(Vector2D other) {
		double x = this.x * other.x;
		double y = this.y * other.y;
		return new Vector2D(x, y);
	}

	public String toString() {
		return (new StringBuffer("[Vector2D x:")).append(x).append(" y:")
				.append(y).append("]").toString();
	}
}
