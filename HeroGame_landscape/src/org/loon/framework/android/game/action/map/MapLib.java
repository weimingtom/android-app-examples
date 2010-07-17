package org.loon.framework.android.game.action.map;

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
public class MapLib {

	public static void setMap(int[][] map, int i) {
		for (int j = 0; j < map.length; j++) {
			for (int k = 0; k < map[0].length; k++)
				map[j][k] = i;

		}

	}

	public static void addMap(int[][] map, int i) {
		if (i == 0)
			return;
		for (int j = 0; j < map.length; j++) {
			for (int k = 0; k < map[0].length; k++)
				map[j][k] += i;

		}

	}

	public static void multiplyMap(int[][] map, float f) {
		if ((double) f == 1.0D)
			return;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				map[i][j] *= f;

		}

	}

	public static void plusMap(int[][] map, int[][] map1) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				map[i][j] = map1[i][j];

		}

	}

	public static void minusMap(int[][] map, int[][] map1) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				map[i][j] = -map1[i][j];

		}

	}

	public static void plusMap(int[][] map, int[][] map1, int i) {
		for (int j = 0; j < map.length; j++) {
			for (int k = 0; k < map[0].length; k++)
				map[j][k] = map1[j][k] + i;

		}

	}

	public static void minusMap(int[][] map, int[][] map1, int i) {
		for (int j = 0; j < map.length; j++) {
			for (int k = 0; k < map[0].length; k++)
				map[j][k] = i - map1[j][k];

		}

	}

	public static void plus2Map(int[][] map, int[][] map1) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				map[i][j] += map1[i][j];

		}

	}

	public static void minus2Map(int[][] map, int[][] map1) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				map[i][j] -= map1[i][j];

		}

	}

	public static void plus2Map(int[][] map, int[][] map1, int i) {
		for (int j = 0; j < map.length; j++) {
			for (int k = 0; k < map[0].length; k++)
				map[j][k] += map1[j][k] + i;

		}

	}

	public static void minus2Map(int[][] map, int[][] map1, int i) {
		for (int j = 0; j < map.length; j++) {
			for (int k = 0; k < map[0].length; k++)
				map[j][k] -= map1[j][k] - i;

		}

	}

	public static void max2Map(int[][] map, int[][] map1) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				if (map[i][j] < map1[i][j])
					map[i][j] = map1[i][j];

		}

	}

	public static void min2Map(int[][] map, int[][] map1) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++)
				if (map[i][j] > map1[i][j])
					map[i][j] = map1[i][j];

		}

	}

	public static void setPos(int[][] map, int i, int j, int k) {
		map[j][i] = k;
	}

	public static void addPos(int[][] map, int i, int j, int k) {
		map[j][i] += k;
	}

	public static void setArea(int[][] map, int[][] map1, int i, int j,
			boolean flag, int k) {
		for (int l = 0; l < map1.length; l++) {
			for (int i1 = 0; i1 < map1[0].length; i1++)
				if (map[l][i1] != 0x186a0)
					if (j == 0 && i >= map1[l][i1])
						map[l][i1] = k;
					else if (j == 1 && i <= map1[l][i1])
						map[l][i1] = k;

		}

	}

	public static void addArea(int[][] map, int[][] map1, int i, int j) {
		if (j == 0)
			return;
		for (int k = 0; k < map1.length; k++) {
			for (int l = 0; l < map1[0].length; l++) {
				if (i >= map1[k][l]) {
					map[k][l] += j;
				}
			}
		}

	}

}
