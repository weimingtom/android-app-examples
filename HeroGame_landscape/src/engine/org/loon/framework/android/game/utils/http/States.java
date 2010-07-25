package org.loon.framework.android.game.utils.http;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class States {
	long lenght;

	float kb;

	int percent;

	public float getKB() {
		return kb;
	}

	public void setKB(float kb) {
		this.kb = kb;
	}

	public long getLenght() {
		return lenght;
	}

	public void setLenght(long lenght) {
		this.lenght = lenght;
	}

	public float getPercentBar() {
		return (float) percent / 100;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

}
