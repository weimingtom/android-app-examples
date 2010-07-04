package org.ssg.android.game.herogame.db;

public class ArchivingManager {

	/**
	 * 将名称为firstArchivingName的存档另存为名称为twoArchivingName的存档
	 * 
	 * @param firstArchivingName
	 * @param twoArchivingName
	 */
	protected static void changeArchivingToAnthorArchiving(
			String firstArchivingName, String twoArchivingName) {

	}

	/**
	 * 删除名称为archivingName的存档
	 * 
	 * @param archivingName
	 */
	protected static void deleteArchiving(String archivingName) {

	}

	/**
	 * 获取所有的存档
	 * 
	 * @return
	 */
	protected static String[] getArchivings() {
		String[] strs = { "Archiving1", "Archiving2", "Archiving3" };
		return strs;
	}
}
