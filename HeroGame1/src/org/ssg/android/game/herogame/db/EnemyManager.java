package org.ssg.android.game.herogame.db;

import org.ssg.android.game.herogame.Enemy;

public class EnemyManager {

	/**
	 * 在名称为archivingName的存档上保存怪，如果存在则修改
	 * 
	 * @param Level
	 *            第几层
	 * @param enemys
	 *            所有的怪
	 * @param archivingName
	 *            存档名称
	 */
	protected static void saveEnemys(int level, Enemy[] enemys,
			String archivingName) {

	}

	/**
	 * 在名称为archivingName的存档上获取怪
	 * 
	 * @param Level
	 *            第几层
	 * @param enemys
	 *            所有的怪
	 * @param archivingName
	 *            存档名称
	 */
	protected static Enemy[] loadEnemys(int level, String archivingName) {
		return null;
	}
}
