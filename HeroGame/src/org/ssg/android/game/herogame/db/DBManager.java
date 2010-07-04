package org.ssg.android.game.herogame.db;

import org.ssg.android.game.herogame.Enemy;
import org.ssg.android.game.herogame.Hero;
import org.ssg.android.game.herogame.db.bean.ArchivingBean;

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
	public static void saveEnemys(int levelNo, Enemy[] enemys,
			String archivingName) {
		EnemyManager.saveEnemys(levelNo, enemys, archivingName);
	}

	/**
	 * 在默认存档上保存怪，如果存在则修改
	 * 
	 * @param Level
	 *            第几层
	 * @param enemys
	 *            所有的怪
	 */
	public static void saveEnemys(int levelNo, Enemy[] enemys) {
		EnemyManager.saveEnemys(levelNo, enemys,
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
	public static Enemy[] loadEnemys(int levelNo, String archivingName) {
		return EnemyManager.loadEnemys(levelNo, archivingName);
	}

	/**
	 * 在默认存档上获取怪
	 * 
	 * @param Level
	 *            第几层
	 */
	public static Enemy[] loadEnemys(int levelNo) {
		return EnemyManager.loadEnemys(levelNo,
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
		ArchivingManager.getInstance().changeArchivingToAnthorArchiving(
				ConstantUtil.autoSaveArchivingName, anthorArchivingName);
	}

	/**
	 * 删除名称为archivingName的存档
	 * 
	 * @param archivingName
	 */
	public static void deleteArchiving(String archivingName) {
		ArchivingManager.getInstance().deleteArchiving(archivingName);
	}

	/**
	 * 获取所有的存档
	 * 
	 * @return
	 */
	public static String[] getArchivings() {
		return ArchivingManager.getInstance().getArchivings();
	}

	/**
	 * 根据index获取ArchivingBean
	 * 
	 * @param index
	 * @return
	 */
	public static ArchivingBean getArchivingBeanByIndex(int index) {
		return ArchivingManager.getInstance().getArchivingBeanByIndex(index);
	}

	/**
	 * 是否已经存在levelNo的自动存档
	 * 
	 * @param levelNo
	 * @return
	 */
	public static boolean hasAutoSaveArchiving(int levelNo) {
		return ArchivingManager.getInstance().hasArchivingFromArchivingName(
				ConstantUtil.autoSaveArchivingName, levelNo);
	}

	/**
	 * 是否已经存在levelNo、archivingName的存档
	 * 
	 * @param archivingName
	 * @param levelNo
	 * @return
	 */
	public static boolean hasArchivingFromArchivingName(String archivingName,
			int levelNo) {
		return ArchivingManager.getInstance().hasArchivingFromArchivingName(
				archivingName, levelNo);
	}

}
