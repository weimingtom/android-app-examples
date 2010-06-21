package org.loon.framework.android.game.core.graphics.window;

import org.loon.framework.android.game.core.graphics.window.achieve.IButton;
import org.loon.framework.android.game.core.graphics.window.achieve.IPanel;

/**
 * 
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

public class LUI extends UIConfig {

	public LUI() {
		this.setupUI(new IPanel());
		this.setupUI(new IButton());

		/*
		 * this.setupUI(new ITag()); this.setupUI(new IPanel());
		 * this.setupUI(new IText()); this.setupUI(new ITool());
		 * this.setupUI(new IToolTip()); this.setupUI(new ITitleBar());
		 * this.setupUI(new ITitleBarButton());
		 */
	}

	public String getName() {
		return "Basic UI";
	}

}
