package org.loon.framework.android.game.media.sound;

import org.loon.framework.android.game.core.LSystem;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
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
 * @version 0.1.2
 */
public class AssetsSound {

	private MediaPlayer player;

	private String name;

	private boolean playing;

	private boolean loop;

	private Context context;

	public AssetsSound(Context ctx) {
		try {
			context = ctx;
			player = new MediaPlayer();
			player
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer mp) {
							playing = false;
							if (loop) {
								mp.start();
							}
						}

					});
		} catch (Exception e) {
		}
	}

	public AssetsSound(String file) {
		this(LSystem.getSystemHandler().getLGameActivity());
		setDataSource(file);
	}

	public AssetsSound(Context ctx, Uri uri) {
		try {
			name = uri.toString();
			context = ctx;
			player = MediaPlayer.create(ctx, uri);
			player
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer mp) {
							playing = false;
							if (loop) {
								mp.start();
							} else {
								mp.stop();
							}
						}
					});
		} catch (Exception e) {
		}
	}

	public void play() {
		try {
			if (playing) {
				player.seekTo(0);
				return;
			}
			if (player != null) {
				playing = true;
				player.start();
			}
		} catch (Exception e) {

		}
	}

	public void setDataSource(String file) {
		try {
			player.setDataSource(context.getAssets().openFd(file)
					.getFileDescriptor(), context.getAssets().openFd(file)
					.getStartOffset(), context.getAssets().openFd(file)
					.getLength());
			player.prepare();
		} catch (Exception e) {

		}

	}

	public void play(int vol) {
		try {
			if (playing) {
				player.seekTo(0);
			}
			if (player != null) {
				playing = true;
				player
						.setVolume((float) Math.log10(vol), (float) Math
								.log(vol));
				player.start();
			}
		} catch (Exception e) {

		}
	}

	public void loop() {
		try {
			if (playing) {
				player.seekTo(0);
			}
			if (player != null) {
				loop = true;
				playing = true;
				player.start();
			}
		} catch (Exception e) {

		}
	}

	public void stop() {
		try {
			loop = false;
			if (playing) {
				playing = false;
				player.pause();
			}
		} catch (Exception e) {
		}
	}

	public void stopPlayer() {
		try {
			loop = false;
			playing = false;
			player.pause();
			player.stop();
		} catch (Exception e) {
		}
	}

	public void reset() {
		try {
			if (player != null) {
				player.reset();
			}
		} catch (Exception e) {

		}
	}

	public void release() {
		try {
			if (player != null) {
				player.release();
				player = null;
			}
		} catch (Exception e) {

		}
	}

	public String getName() {
		return name;
	}

	public void setVolume(int vol) {
		try {
			if (player != null) {
				player.setVolume((float) Math.log10(vol), (float) Math
						.log10(vol));
			}
		} catch (Exception e) {
		}
	}

}
