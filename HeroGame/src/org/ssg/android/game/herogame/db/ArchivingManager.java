package org.ssg.android.game.herogame.db;

import java.util.LinkedList;

import org.ssg.android.game.herogame.db.bean.ArchivingBean;

public class ArchivingManager {
	
	private LinkedList<ArchivingBean> archivings = new LinkedList<ArchivingBean>();
	
	private static ArchivingManager instance = new ArchivingManager();
	private ArchivingManager(){ 
	}
	public static ArchivingManager getInstance(){
		return instance;
	}

	/**
	 * 将名称为firstArchivingName的存档另存为名称为twoArchivingName的存档
	 * 
	 * @param firstArchivingName
	 * @param twoArchivingName
	 */
	protected void changeArchivingToAnthorArchiving(
			String firstArchivingName, String twoArchivingName) {

	}

	/**
	 * 删除名称为archivingName的存档
	 * 
	 * @param archivingName
	 */
	protected void deleteArchiving(String archivingName) {

	}
	
	private void init(){
		archivings.clear();
		for (int i=0;i<3;i++){
			ArchivingBean archivingBean = new ArchivingBean();
			archivingBean.setId(""+i);
			archivingBean.setName("Archiving"+i);
			archivings.add(archivingBean);
		}
	}
	

	/**
	 * 获取所有的存档
	 * 
	 * @return
	 */
	protected String[] getArchivings() {
		init();
		String[] strs = new String[archivings.size()];
		for (int i=0;i<strs.length;i++){
			strs[i] = archivings.get(i).getName();
		}
		return strs;
	}
	
	/**
	 * 根据index获取ArchivingBean
	 * 
	 * @return
	 */
	protected ArchivingBean getArchivingBeanByIndex(int index) {
		return archivings.get(index);
	}
}
