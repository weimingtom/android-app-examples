package org.loon.framework.android.game.action.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Field2D implements Config {

	final static private Map<Vector2D,Integer> directions = new HashMap<Vector2D,Integer>(8);

	final static private Map<Integer,Vector2D> directionValues = new HashMap<Integer,Vector2D>(8);

	static {
		directions.put(new Vector2D(1, -1), new Integer(Config.UP));
		directions.put(new Vector2D(-1, -1), new Integer(Config.LEFT));
		directions.put(new Vector2D(1, 1), new Integer(Config.RIGHT));
		directions.put(new Vector2D(-1, 1), new Integer(Config.DOWN));
		directions.put(new Vector2D(0, -1), new Integer(Config.TUP));
		directions.put(new Vector2D(-1, 0), new Integer(Config.TLEFT));
		directions.put(new Vector2D(1, 0), new Integer(Config.TRIGHT));
		directions.put(new Vector2D(0, 1), new Integer(Config.TDOWN));

		directionValues.put(new Integer(Config.UP), new Vector2D(1, -1));
		directionValues.put(new Integer(Config.LEFT), new Vector2D(-1, -1));
		directionValues.put(new Integer(Config.RIGHT), new Vector2D(1, 1));
		directionValues.put(new Integer(Config.DOWN), new Vector2D(-1, 1));
		directionValues.put(new Integer(Config.TUP), new Vector2D(0, -1));
		directionValues.put(new Integer(Config.TLEFT), new Vector2D(-1, 0));
		directionValues.put(new Integer(Config.TRIGHT), new Vector2D(1, 0));
		directionValues.put(new Integer(Config.TDOWN), new Vector2D(0, 1));

	}

	private int[][] data;

	public Field2D(int[][] data) {
		this.data = (int[][]) data.clone();

	}

	public boolean isHit(Vector2D point) {
		if (get(data, point) != 1) {
			return true;
		}
		return false;
	}

	public static int getDirection(int x, int y) {
		return ((Integer) directions.get(new Vector2D(x, y))).intValue();
	}

	public static Vector2D getDirection(int type) {
		return (Vector2D) directionValues.get(new Integer(type));
	}

	public List<Vector2D> neighbors(Vector2D pos) {

		List<Vector2D> result = new ArrayList<Vector2D>(8);

		int x = pos.x();
		int y = pos.y();

		result.add(new Vector2D(x, y - 1));
		result.add(new Vector2D(x + 1, y - 1));
		result.add(new Vector2D(x + 1, y));
		result.add(new Vector2D(x + 1, y + 1));
		result.add(new Vector2D(x, y + 1));
		result.add(new Vector2D(x - 1, y + 1));
		result.add(new Vector2D(x - 1, y));
		result.add(new Vector2D(x - 1, y - 1));

		return result;

	}

	public int score(Vector2D goal, Vector2D point) {
		return Math.abs(point.x() - goal.x()) + Math.abs(point.y() - goal.y());
	}

	private int get(int[][] data, Vector2D point) {
		try {
			return data[point.y()][point.x()];
		} catch (Exception e) {
			return 1;
		}
	}

}
