package org.ssg.android.game.herogame.db;

import org.ssg.android.game.herogame.Enemy;
import org.ssg.android.game.herogame.Hero;

public class DBManager {
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
	public static void saveEnemys(int level, Enemy[] enemys,
			String archivingName) {
		EnemyManager.saveEnemys(level, enemys, archivingName);
	}

	/**
	 * 在默认存档上保存怪，如果存在则修改
	 * 
	 * @param Level
	 *            第几层
	 * @param enemys
	 *            所有的怪
	 */
	public static void saveEnemys(int level, Enemy[] enemys) {
		EnemyManager.saveEnemys(level, enemys,
				ConstantUtil.autoSaveArchivingName);
	}

	/**
	 * 在名称为archivingName的存档上获取怪
	 * 
	 * @param Level
	 *            第几层
	 * @param archivingName
	 *            存档名称
	 */
	public static Enemy[] loadEnemys(int level, String archivingName) {
		return EnemyManager.loadEnemys(level, archivingName);
	}

	/**
	 * 在默认存档上获取怪
	 * 
	 * @param Level
	 *            第几层
	 */
	public static Enemy[] loadEnemys(int level) {
		return EnemyManager.loadEnemys(level,
				ConstantUtil.autoSaveArchivingName);
	}

	/**
	 * 在名称为archivingName的存档上初始化英雄，如果存在则修改
	 * 
	 * @param Level
	 *            第几层
	 * @param enemys
	 *            英雄
	 * @param archivingName
	 *            存档名称
	 */
	public static void saveHero(Hero hero, String archivingName) {
		HeroManger.saveHero(hero, archivingName);
	}

	/**
	 * 在默认存档上初始化英雄，如果存在则修改
	 * 
	 * @param Level
	 *            第几层
	 * @param enemys
	 *            英雄
	 */
	public static void saveHero(Hero hero) {
		HeroManger.saveHero(hero, ConstantUtil.autoSaveArchivingName);
	}

	/**
	 * 在名称为archivingName的存档上获取英雄
	 * 
	 * @param archivingName
	 *            存档名称
	 */
	public static Hero loadHero(String archivingName) {
		return HeroManger.loadHero(archivingName);
	}

	/**
	 * 在默认存档上获取英雄
	 */
	public static Hero loadHero() {
		return HeroManger.loadHero(ConstantUtil.autoSaveArchivingName);
	}

	/**
	 * 将默认保存的存档保存为anthorArchivingName存档
	 * 
	 * @param anthorArchivingName
	 */
	public static void changeAutoSaveArchivingToAnthorArchiving(
			String anthorArchivingName) {
		ArchivingManager.changeArchivingToAnthorArchiving(
				ConstantUtil.autoSaveArchivingName, anthorArchivingName);
	}

	/**
	 * 删除名称为archivingName的存档
	 * 
	 * @param archivingName
	 */
	public static void deleteArchiving(String archivingName) {
		ArchivingManager.deleteArchiving(archivingName);
	}

	/**
	 * 获取所有的存档
	 * 
	 * @return
	 */
	public static String[] getArchivings() {
		return ArchivingManager.getArchivings();
	}

}
