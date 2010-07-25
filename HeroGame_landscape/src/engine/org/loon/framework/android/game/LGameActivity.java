package org.loon.framework.android.game;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpException;
import org.loon.framework.android.game.core.Handler;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.LImage;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 
 * Copyright 2008 - 2010
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
 * @version 0.1.0
 */
public class LGameActivity extends Activity {

	// private final static float LGAME_TARGET_HEAP_UTILIZATION = 0.75f;

	// private final static int LGAME_HEAP_SIZE = 6 * 1024 * 1024;

	private static final String dealutKeywords = "app android game home happy mobile play classic video love";

	private boolean visible, keyboardOpen, isLandscape;

	private int androidSelect, orientation;

	private AlertDialog alert;

	private LGameView view;

	private AdView adview;

	private Handler gameHandler;

	private FrameLayout frameLayout;

	public void initialization(Bundle icicle) {
		initialization(icicle, false, false, null);
	}

	public void initialization(Bundle icicle, boolean landscape) {
		initialization(icicle, landscape, false, null);
	}

	public void initialization(Bundle icicle, boolean landscape,
			boolean openAD, String publisherId) {
		initialization(icicle, landscape, openAD, LAD.BOTTOM, publisherId);
	}

	public void initialization(Bundle icicle, boolean landscape,
			String publisherId) {
		initialization(icicle, landscape, true, LAD.BOTTOM, publisherId);
	}

	public void initialization(Bundle icicle, LAD lad, String publisherId) {
		initialization(icicle, false, true, lad, publisherId);
	}

	public void initialization(Bundle icicle, boolean landscape, LAD lad,
			String publisherId) {
		initialization(icicle, landscape, true, lad, publisherId);
	}

	public void initialization(Bundle icicle, boolean landscape,
			boolean openAD, LAD lad, String publisherId) {
		initialization(icicle, landscape, openAD, lad, publisherId,
				dealutKeywords);
	}

	public void initialization(Bundle icicle, LAD lad, String publisherId,
			int requestInterval) {
		initialization(icicle, false, lad, publisherId, requestInterval);
	}

	public void initialization(Bundle icicle, boolean landscape, LAD lad,
			String publisherId, int requestInterval) {
		initialization(icicle, landscape, true, lad, publisherId,
				dealutKeywords, requestInterval);
	}

	public void initialization(Bundle icicle, boolean landscape, LAD lad,
			String publisherId, String keywords) {
		initialization(icicle, landscape, true, lad, publisherId, keywords, 60);
	}

	public void initialization(Bundle icicle, boolean landscape, LAD lad,
			String publisherId, String keywords, int requestInterval) {
		initialization(icicle, landscape, true, lad, publisherId, keywords,
				requestInterval);
	}

	public void initialization(Bundle icicle, boolean landscape,
			boolean openAD, LAD lad, String publisherId, String keywords) {
		initialization(icicle, landscape, openAD, lad, publisherId, keywords,
				60);
	}

	public void initialization(Bundle icicle, final boolean landscape,
			final boolean openAD, final LAD lad, final String publisherId,
			final String keywords, final int requestInterval) {
		LSystem.gc();
		this.androidSelect = -2;
		frameLayout = new FrameLayout(LGameActivity.this);
		super.onCreate(icicle);
		if (openAD) {
			// setVolumeControlStream(AudioManager.STREAM_MUSIC);
			AdManager.setPublisherId(publisherId);
			AdManager.setTestDevices(new String[] { "" });
			AdManager.setAllowUseOfLocation(true);
			view = new LGameView(LGameActivity.this, landscape);
			adview = new AdView(LGameActivity.this);
			adview.setKeywords(keywords);
			adview.setRequestInterval(requestInterval);
			adview.setGravity(Gravity.NO_GRAVITY);
			RelativeLayout rl = new RelativeLayout(LGameActivity.this);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			if (lad == LAD.LEFT) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
			} else if (lad == LAD.RIGHT) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			} else if (lad == LAD.TOP) {
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			} else {
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
			}
			visible = true;
			adview.requestFreshAd();
			frameLayout.addView(view);
			rl.addView(adview, lp);
			frameLayout.addView(rl);
		} else {
			view = new LGameView(LGameActivity.this, landscape);
			frameLayout.addView(view);
		}
		if (view != null) {
			gameHandler = view.getGameHandler();
		}

	}

	public void showScreen() {
		setContentView(frameLayout);
		try {
			getWindow().setBackgroundDrawable(null);
		} catch (Exception e) {
		}
	}

	public FrameLayout getFrameLayout() {
		return frameLayout;
	}

	public void initialization(Bundle icicle, IScreen screen, int maxFps) {
		this.initialization(icicle);
		this.setScreen(screen);
		this.setFPS(maxFps);
		this.setShowFPS(false);
	}

	public void initialization(Bundle icicle, IScreen screen) {
		this.initialization(icicle, screen, LSystem.DEFAULT_MAX_FPS);
	}

	/**
	 * 获得当前环境版本信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		try {
			String packName = getPackageName();
			return getPackageManager().getPackageInfo(packName, 0);
		} catch (Exception ex) {

		}
		return null;
	}

	/**
	 * 获得当前版本名
	 * 
	 * @return
	 */
	public String getVersionName() {
		PackageInfo info = getPackageInfo();
		if (info != null) {
			return info.versionName;
		}
		return null;
	}

	/**
	 * 获得当前版本号
	 * 
	 * @return
	 */
	public int getVersionCode() {
		PackageInfo info = getPackageInfo();
		if (info != null) {
			return info.versionCode;
		}
		return -1;
	}

	/**
	 * 判断当前环境是否已连接网络
	 * 
	 * @return
	 */
	public boolean isNetwork() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * 广告组件是否显示
	 * 
	 * @return
	 */
	public boolean isVisibleAD() {
		return visible;
	}

	/**
	 * 隐藏广告
	 */
	public void hideAD() {
		try {
			if (adview != null) {
				Runnable runnable = new Runnable() {
					public void run() {
						adview.setVisibility(View.GONE);
						visible = false;
					}
				};
				runOnUiThread(runnable);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 显示广告
	 */
	public void showAD() {
		try {
			if (adview != null) {
				Runnable runnable = new Runnable() {
					public void run() {
						adview.setVisibility(View.VISIBLE);
						adview.requestFreshAd();
						visible = true;
					}
				};
				runOnUiThread(runnable);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 界面同步的浏览器访问
	 * 
	 * @param url
	 */
	public void openBrowser(final String url) {
		runOnUiThread(new Runnable() {
			public void run() {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}

	/**
	 * 选择的结果
	 * 
	 * @return
	 */
	public int getAndroidSelect() {
		return androidSelect;
	}

	/**
	 * 界面同步的Progress弹出
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public AlertDialog showAndroidProgress(final String title,
			final String message) {
		if (alert == null || !alert.isShowing()) {
			Runnable showProgress = new Runnable() {
				public void run() {
					alert = ProgressDialog.show(LGameActivity.this, title,
							message);
				}
			};
			runOnUiThread(showProgress);
		}
		return alert;
	}

	/**
	 * 界面同步的Alter弹出
	 * 
	 * @param message
	 */
	public void showAndroidAlert(final String title, final String message) {
		Runnable showAlert = new Runnable() {
			public void run() {
				final AlertDialog alert = new AlertDialog.Builder(
						LGameActivity.this).create();
				alert.setTitle(title);
				alert.setMessage(message);
				alert.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						alert.dismiss();
					}
				});
				alert.show();
			}
		};
		runOnUiThread(showAlert);
	}

	/**
	 * 界面同步的Select弹出
	 * 
	 * @param title
	 * @param text
	 * @return
	 */
	public int showAndroidSelect(final String title, final String text[]) {
		Runnable showSelect = new Runnable() {
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						LGameActivity.this);
				builder.setTitle(title);
				builder.setItems(text, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						androidSelect = item;
					}
				});
				builder
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								androidSelect = -1;
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		};
		runOnUiThread(showSelect);
		return androidSelect;
	}

	/**
	 * 界面同步的Exception弹出
	 * 
	 * @param ex
	 */
	public void showAndroidException(final Exception ex) {
		runOnUiThread(new Runnable() {
			public void run() {
				androidException(ex);
			}
		});
	}

	/**
	 * 以对话框方式显示一个Android异常
	 * 
	 * @param exception
	 */
	private void androidException(Exception exception) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		try {
			throw exception;
		} catch (IOException e) {
			if (e.getMessage().startsWith("Network unreachable")) {
				builder.setTitle("No network");
				builder
						.setMessage("LGame-Android Remote needs local network access. Please make sure that your wireless network is activated. You can click on the Settings button below to directly access your network settings.");
				builder.setNeutralButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							}
						});
			} else {
				builder.setTitle("Unknown I/O Exception");
				builder.setMessage(e.getMessage().toString());
			}
		} catch (HttpException e) {
			if (e.getMessage().startsWith("401")) {
				builder.setTitle("HTTP 401: Unauthorized");
				builder
						.setMessage("The supplied username and/or password is incorrect. Please check your settings.");
				builder.setNeutralButton("Settings",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent());
							}
						});
			}
		} catch (RuntimeException e) {
			builder.setTitle("RuntimeException");
			builder.setMessage(e.getMessage());
		} catch (Exception e) {
			builder.setTitle("Exception");
			builder.setMessage(e.getMessage());
		} finally {
			exception.printStackTrace();
			builder.setCancelable(true);
			builder.setNegativeButton("Close",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			final AlertDialog alert = builder.create();
			try {
				alert.show();
			} catch (Throwable e) {
			} finally {
				LSystem.destroy();
			}
		}
	}

	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		orientation = config.orientation;
		keyboardOpen = config.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO;
		isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	public void setScreen(IScreen screen) {
		if (view != null) {
			this.view.setScreen(screen);
		}
	}

	public void setShowFPS(boolean flag) {
		if (view != null) {
			this.view.setShowFPS(flag);
		}
	}
	
	public void setLogo(LImage img) {
		if (view != null) {
			this.view.setLogo(img);
		}
	}

	public LImage getLogo() {
		if (view != null) {
			return this.view.getLogo();
		}
		return null;
	}
	
	public void setFPS(long frames) {
		if (view != null) {
			this.view.setFPS(frames);
		}
	}

	public LGameView view() {
		return view;
	}

	public AdView adView() {
		return adview;
	}

	protected void onPause() {
		if (view != null) {
			view.setPaused(true);
		}
		super.onPause();
	}

	protected void onResume() {
		if (view != null) {
			view.setPaused(false);
		}
		super.onResume();
	}

	protected void onDestroy() {
		try {
			if (view != null) {
				view.setRunning(false);
				if (adview != null) {
					adview = null;
				}
			}
			super.onDestroy();
			// 双保险
//			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e) {

		}
	}

	protected void onStop() {
		try {
			if (view != null) {
				this.view.setPaused(true);
			}
			super.onStop();
		} catch (Exception e) {

		}

	}

	public boolean isShowLogo() {
		if (view != null) {
			return view.isShowLogo();
		}
		return false;
	}

	public void setShowLogo(boolean showLogo) {
		if (view != null) {
			view.setShowLogo(showLogo);
		}
	}

	public void setDebug(boolean debug) {
		if (view != null) {
			this.view.setDebug(true);
		}
	}

	public boolean isDebug() {
		if (view != null) {
			return this.view.isDebug();
		}
		return false;
	}

	/**
	 * 键盘是否已显示
	 * 
	 * @return
	 */
	public boolean isKeyboardOpen() {
		return keyboardOpen;
	}

	/**
	 * 是否使用了横屏
	 * 
	 * @return
	 */
	public boolean isLandscape() {
		return isLandscape;
	}

	/**
	 * 当前窗体方向
	 * 
	 * @return
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * 获得指定的Asset资源
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public InputStream openAssetResource(String fileName) throws IOException {
		AssetManager assetmanager = getAssets();
		boolean flag = fileName.startsWith("/");
		String file;
		if (flag) {
			file = fileName.substring(1);
		} else {
			file = fileName;
		}
		return assetmanager.open(file);
	}

	/**
	 * 退出当前应用
	 */
	public void close() {
		finish();
	}

	// PS:似乎由于多线程的关系，在某些手机机型中由LGameView引向Screen类的监听器可能会出错（某用户反馈按钮无响应，不知真假……）|||，
	// 只好直接转外部事件，却不知道效果如何(留言那家伙也不知道死哪去了，我自己测怎么跑都没事|||)……这种方式如果大家测着有事请告诉小弟，我再改回监听……
	public boolean onTouchEvent(MotionEvent e) {
		boolean ret = super.onTouchEvent(e);
		try {
			if (gameHandler != null) {
				return gameHandler.onTouchEvent(e);
			}
		} catch (Exception ex) {

		}
		return ret;
	}

	public boolean onKeyDown(int keyCode, KeyEvent e) {
		boolean ret = super.onKeyDown(keyCode, e);
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				LSystem.exit();
				return false;
			}
			if (gameHandler != null) {
				return gameHandler.onKeyDown(keyCode, e);
			}
		} catch (Exception ex) {

		}
		return ret;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		boolean ret = super.onKeyUp(keyCode, e);
		try {
			if (gameHandler != null) {
				return gameHandler.onKeyUp(keyCode, e);
			}
		} catch (Exception ex) {

		}
		return ret;
	}
}