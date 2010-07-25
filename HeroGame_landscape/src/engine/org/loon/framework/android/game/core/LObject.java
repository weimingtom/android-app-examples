package org.loon.framework.android.game.core;

import org.loon.framework.android.game.action.map.Field2D;
import org.loon.framework.android.game.action.map.Vector2D;
import org.loon.framework.android.game.core.graphics.LGraphics;

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
public abstract class LObject {

	protected Vector2D location = new Vector2D(0, 0);

	protected int layer;

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void move_45D_up() {
		move_45D_up(1);
	}

	public void move_45D_up(int multiples) {
		location.move_multiples(Field2D.UP, multiples);
	}

	public void move_45D_left() {
		move_45D_left(1);
	}

	public void move_45D_left(int multiples) {
		location.move_multiples(Field2D.LEFT, multiples);
	}

	public void move_45D_right() {
		move_45D_right(1);
	}

	public void move_45D_right(int multiples) {
		location.move_multiples(Field2D.RIGHT, multiples);
	}

	public void move_45D_down() {
		move_45D_down(1);
	}

	public void move_45D_down(int multiples) {
		location.move_multiples(Field2D.DOWN, multiples);
	}

	public void move_up() {
		move_up(1);
	}

	public void move_up(int multiples) {
		location.move_multiples(Field2D.TUP, multiples);
	}

	public void move_left() {
		move_left(1);
	}

	public void move_left(int multiples) {
		location.move_multiples(Field2D.TLEFT, multiples);
	}

	public void move_right() {
		move_right(1);
	}

	public void move_right(int multiples) {
		location.move_multiples(Field2D.TRIGHT, multiples);
	}

	public void move_down() {
		move_down(1);
	}

	public void move_down(int multiples) {
		location.move_multiples(Field2D.TDOWN, multiples);
	}

	public void move(Vector2D vector2D) {
		location.move(vector2D);
	}

	public void move(double x, double y) {
		location.move(x, y);
	}

	public void setLocation(double x, double y) {
		location.setLocation(x, y);
	}

	public int x() {
		return (int) location.getX();
	}

	public int y() {
		return (int) location.getY();
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public void setX(Integer x) {
		location.setX(x.intValue());
	}

	public void setX(double x) {
		location.setX(x);
	}

	public void setY(Integer y) {
		location.setY(y.intValue());
	}

	public void setY(double y) {
		location.setY(y);
	}

	public Vector2D getLocation() {
		return location;
	}

	public void setLocation(Vector2D location) {
		this.location = location;
	}

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void update(long timer);

	public abstract void createUI(LGraphics g);

}
