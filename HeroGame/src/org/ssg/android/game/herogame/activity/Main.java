package org.ssg.android.game.herogame.activity;

import org.ssg.android.game.herogame.R;
import org.ssg.android.game.herogame.db.ConstantUtil;
import org.ssg.android.game.herogame.db.DBManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Main extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		View newButton = findViewById(R.id.newGameButton);
		View continueButton = findViewById(R.id.selectArchivingButton);
		View exitButton = findViewById(R.id.exitGameButton);

		newButton.setOnClickListener(this);
		continueButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}
	
	private void selectArchiving() {
		AlertDialog.Builder selectArchivingAlertDialog = new AlertDialog.Builder(this);
		selectArchivingAlertDialog.setTitle("Select Archiving...");
		String[] items = DBManager.getArchivings();//{"Archiving1","Archiving2","Archiving3"};
		selectArchivingAlertDialog.setItems(items, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int i) {
				Intent intent = new Intent(Main.this, HeroGame.class);
				intent.putExtra("archivingId", i);//默认的存档ID
				startActivity(intent);
			}});
		selectArchivingAlertDialog.create().show();
        
		return;
	}
	
	private void newGame() {
		Intent intent = new Intent(Main.this, HeroGame.class);
		intent.putExtra("archivingId", ConstantUtil.autoSaveArchivingId);//默认的存档ID
		startActivity(intent);
		return ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectArchivingButton:
			selectArchiving();
			break;
		case R.id.newGameButton:
			newGame();
			break;
		case R.id.exitGameButton:
			finish();
			break;
		}
	}
}