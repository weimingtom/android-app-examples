package org.ssg.android.game.herogame.activity;

import org.loon.framework.android.game.LGameActivity;
import org.ssg.android.game.herogame.MainScreen;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

public class HeroGame extends LGameActivity {

    @Override
    public void onCreate(Bundle icicle) {
    	super.onCreate(icicle);
    	this.initialization(icicle, false);
    	 
    	Intent intent = getIntent();
    	MainScreen mainScreen = new MainScreen();
    	mainScreen.setArchivingId(intent.getStringExtra("archivingId"));//存档ID
    	
        // 游戏主窗体
        this.setScreen(mainScreen);
        // 刷新率
        this.setFPS(30);
        // 是否显示刷新率
        this.setShowFPS(false);
        // 是否显示logo
        this.setShowLogo(false);
        // 显示窗体
        this.showScreen();
    }

    //转屏时防止调用onCreate
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // land
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // port
            }
        } catch (Exception ex) {
        }

    }

}