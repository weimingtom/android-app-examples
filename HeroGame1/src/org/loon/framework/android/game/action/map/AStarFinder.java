package org.loon.framework.android.game.action.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class AStarFinder {

	private Field2D map;

	private Vector2D goal;

	private List pathes;

	private Set visitedCache;

	public static List find(Field2D maps, Vector2D start, Vector2D goal) {
		AStarFinder astar = new AStarFinder();
		List path = astar.calc(maps, start, goal);
		return path;
	}

	public static List find(int[][] maps, Vector2D start, Vector2D goal) {
		return AStarFinder.find(new Field2D(maps), start, goal);
	}

	public List calc(Field2D map, Vector2D start, Vector2D goal) {
		return new AStarFinder().solve(map, start, goal);
	}

	private List solve(Field2D map, Vector2D start, Vector2D goal) {
		this.map = map;
		this.goal = goal;
		visitedCache = new HashSet();
		pathes = new LinkedList();
		visitedCache.add(start);
		LinkedList path = new LinkedList();
		path.add(start);
		ScoredPath spath = new ScoredPath(0, path);
		pathes.add(spath);
		return astar();
	}

	private List astar() {
		while (pathes.size() > 0) {
			ScoredPath spath = (ScoredPath) pathes.remove(0);
			Vector2D current = (Vector2D) spath.path.get(spath.path.size() - 1);
			if (current.equals(goal)) {
				return spath.path;
			}
			for (Iterator it = map.neighbors(current).iterator(); it.hasNext();) {
				Vector2D next = (Vector2D) it.next();
				if (visitedCache.contains(next)) {
					continue;
				}
				visitedCache.add(next);
				if (!map.isHit(next)) {
					continue;
				}
				ArrayList path = new ArrayList(spath.path);
				path.add(next);
				int score = spath.score + map.score(goal, next);
				insert(score, path);
			}
		}
		return null;
	}

	private void insert(int score, ArrayList path) {
		for (int i = 0; i < pathes.size(); i += 1) {
			ScoredPath spath = (ScoredPath) pathes.get(i);
			if (spath.score >= score) {
				pathes.add(i, new ScoredPath(score, path));
				return;
			}
		}
		pathes.add(new ScoredPath(score, path));
	}

	private class ScoredPath {

		private int score;

		private List path;

		ScoredPath(int score, List path) {
			this.score = score;
			this.path = path;
		}
	}
}
