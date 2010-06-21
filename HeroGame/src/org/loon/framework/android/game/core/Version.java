package org.loon.framework.android.game.core;

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
public final class Version {

	private final int major;

	private final int minor;

	private final int build;

	/**
	 * 构造版本信息
	 * 
	 * @param major
	 * @param minor
	 * @param build
	 */
	Version(final int major, final int minor, final int build) {
		this.major = major;
		this.minor = minor;
		this.build = build;
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Version) {
			final Version v = (Version) obj;
			return (this.major == v.getMajor()) && (this.minor == v.getMinor())
					&& (this.build == v.getBuild());
		}
		return false;
	}

	public String getAboutString() {
		return LSystem.FRAMEWORK + "-" + this.getVersionString();
	}

	public int getBuild() {
		return this.build;
	}

	public int getMajor() {
		return this.major;
	}

	public int getMinor() {
		return this.minor;
	}

	public String getVersionString() {
		return this.major + "." + this.minor + "." + this.build;
	}

	public boolean isNewerThan(final Version v) {
		if (this.major > v.getMajor()) {
			return true;
		} else if (this.major < v.getMajor()) {
			return false;
		}
		if (this.minor > v.getMinor()) {
			return true;
		} else if (this.minor < v.getMinor()) {
			return false;
		}
		if (this.build > v.getBuild()) {
			return true;
		}
		return false;
	}

	public String toString() {
		return this.getVersionString();
	}
}