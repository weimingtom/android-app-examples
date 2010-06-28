package org.loon.framework.android.game.core.graphics.window.achieve;

import org.loon.framework.android.game.action.map.Vector2D;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;

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
 * @version 0.1.1
 */
public class IPrint {

	private char[] showMessages = new char[] { '\0' };

	private LColor fontColor = LColor.white;

	private int interceptMaxString = 0;

	private int interceptCount = 0;

	private int messageLength = 10;

	private String messages;

	private boolean onComplete, newLine, visible;

	private StringBuffer messageBuffer = new StringBuffer(messageLength);

	private int width, height, leftOffset, topOffset, next, nowLeft,
			messageCount;

	private float alpha;

	private Vector2D vector;

	private LImage creeseIcon;

	public IPrint(Vector2D vector, int width, int height) {
		this("", vector, width, height);
	}

	public IPrint(String context, Vector2D vector, int width, int height) {
		this.setMessage(context);
		this.vector = vector;
		this.width = width;
		this.height = height;
		this.visible = true;
	}

	public void setMessage(String context) {
		setMessage(context, false);
	}

	public void setMessage(String context, boolean isComplete) {
		this.messages = context;
		this.next = context.length();
		this.onComplete = false;
		this.newLine = false;
		this.messageCount = 0;
		this.messageBuffer.delete(0, messageBuffer.length());
		if (isComplete) {
			this.complete();
		}
	}

	public String getMessage() {
		return messages;
	}

	private LColor getColor(char flagName) {
		if ('r' == flagName || 'R' == flagName) {
			return LColor.red;
		}
		if ('b' == flagName || 'B' == flagName) {
			return LColor.black;
		}
		if ('l' == flagName || 'L' == flagName) {
			return LColor.blue;
		}
		if ('g' == flagName || 'G' == flagName) {
			return LColor.green;
		}
		if ('o' == flagName || 'O' == flagName) {
			return LColor.orange;
		}
		if ('y' == flagName || 'Y' == flagName) {
			return LColor.yellow;
		} else {
			return null;
		}
	}

	public void draw(LGraphics g) {
		draw(g, LColor.white);
	}

	private void drawMessage(LGraphics g, LColor old) {
		int fontSize = g.getFont().getSize();
		int fontHeight = g.getFont().getTextHeight();
		nowLeft = (width / 2) - ((fontSize * messageLength) / 2)
				- (fontSize / 2);
		int size = showMessages.length;
		int index = 0, offset = 0;
		for (int i = 0; i < size; i++) {
			if (interceptCount < interceptMaxString) {
				interceptCount++;
				g.setColor(fontColor);
				continue;
			} else {
				interceptMaxString = 0;
				interceptCount = 0;
			}
			if (showMessages[i] == '\n') {
				index = 0;
				offset++;
				continue;
			} else if (showMessages[i] == '<') {
				LColor color = getColor(showMessages[i < size - 1 ? i + 1 : i]);
				if (color != null) {
					interceptMaxString = 1;
					fontColor = color;
				}
				next();
				continue;
			} else if (showMessages[i] == '/') {
				if (showMessages[i < size - 1 ? i + 1 : i] == '>') {
					interceptMaxString = 1;
					fontColor = old;
				}
				continue;
			} else if (index > messageLength) {
				index = 0;
				offset++;
				newLine = false;
			}
			int fontLeft = nowLeft + (fontSize * index);
			if (i != size - 1) {
				g.setAntiAlias(true);
				g.drawString(String.valueOf(showMessages[i]), vector.x()
						+ fontLeft + leftOffset, (offset * fontHeight)
						+ vector.y() + (fontSize * 2) + topOffset);
				g.setAntiAlias(false);
			} else if (!newLine) {
				g.drawImage(creeseIcon, vector.x() + fontLeft + leftOffset,
						(offset * fontHeight) + vector.y() + (fontSize * 2)
								+ topOffset);
			}
			index++;
		}
		if (messageCount == next) {
			onComplete = true;
		}

	}

	public void draw(LGraphics g, LColor old) {
		if (!visible) {
			return;
		}
		alpha = g.getAlpha();
		g.setAlpha(1.0f);
		drawMessage(g, old);
		g.setAlpha(alpha);
	}

	public void complete() {
		onComplete = true;
		messageCount = messages.length();
		next = messageCount;
		showMessages = messages.toCharArray();
	}

	public boolean isComplete() {
		return onComplete;
	}

	public boolean next() {
		if (!onComplete) {
			if (messageCount == next) {
				onComplete = true;
				return false;
			}
			if (messageBuffer.length() > 0) {
				messageBuffer.delete(messageBuffer.length() - 1, messageBuffer
						.length());
			}
			messageBuffer.append(messages.charAt(messageCount));
			messageBuffer.append("_");
			showMessages = messageBuffer.toString().toCharArray();
			messageCount++;
		} else {
			return false;
		}
		return true;
	}

	public LImage getCreeseIcon() {
		return creeseIcon;
	}

	public void setCreeseIcon(LImage creeseIcon) {
		this.creeseIcon = creeseIcon;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLeftOffset() {
		return leftOffset;
	}

	public void setLeftOffset(int leftOffset) {
		this.leftOffset = leftOffset;
	}

	public int getTopOffset() {
		return topOffset;
	}

	public void setTopOffset(int topOffset) {
		this.topOffset = topOffset;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
