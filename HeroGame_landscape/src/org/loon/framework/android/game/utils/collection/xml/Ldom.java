package org.loon.framework.android.game.utils.collection.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.StringUtils;
import org.loon.framework.android.game.utils.collection.ArrayMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
public class Ldom extends ReferenceLdom {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1982090120964078135L;

	private String nodeName;

	private String nodeValue;

	private String comment;

	private Ldom parent;

	private Object eigenValue;

	private String encoding;

	private Ldom previous;

	private Ldom next;

	private Iterator iterator;

	private boolean isNodeClose;

	private StringBuffer xmlBuffer;

	private Node node;

	public Ldom() {
		init();
	}

	public void init() {
		isNodeClose = true;
		xmlBuffer = new StringBuffer();
	}

	public Ldom(DataObject data) {
		super(data);
		init();
	}

	public Ldom(String nodeName) {
		isNodeClose = true;
		this.nodeName = nodeName;
		init();
	}

	public Ldom(String nodeName, String childName) {
		init();
		this.nodeName = nodeName;
		addChild(childName);
	}

	public Ldom(String nodeName, Ldom parent) {
		init();
		this.nodeName = nodeName;
		parent.addChild(this);
		this.parent = parent;
	}

	public Ldom(Node node) {
		init();
		nodeName = node.getNodeName();
		setAttributes(node);
	}

	public boolean isNodeClose() {
		return isNodeClose;
	}

	public void setNodeClose(boolean isNodeClose) {
		this.isNodeClose = isNodeClose;
	}

	public void copyAll(Ldom source) {
		nodeName = source.getNodeName();
		nodeValue = source.getNodeValue();
		removeChildren();
		copy(source);
	}

	public Ldom makeChild(String nodeName) {
		Ldom child = select(nodeName);
		if (child == null) {
			child = newChild(nodeName);
		}
		return child;
	}

	public Ldom makeChild(String nodeName, String key, String value) {
		Ldom child = select(nodeName, key, value);
		if (child == null) {
			child = newChild(nodeName);
			child.set(key, value);
		}
		return child;
	}

	public Ldom newFirstChild(String nodeName) {
		Ldom child = new Ldom(nodeName);
		child.setParent(this);
		addChild(0, child);
		int size = size();
		if (size > 1)
			child.setNext((Ldom) getObject(1));
		return child;
	}

	public Ldom newChild(String nodeName) {
		Ldom child = new Ldom(nodeName);
		child.setParent(this);
		addChild(child);
		int size = size();
		if (size > 1) {
			Ldom previous = (Ldom) getObject(size - 2);
			child.setPrevious(previous);
			previous.setNext(child);
		}
		return child;
	}

	public Ldom newChild(String nodeName, String name, String value) {
		Ldom child = newChild(nodeName);
		child.set(name, value);
		return child;
	}

	public void addChild(String nodeName) {
		addChild(new Ldom(nodeName));
	}

	public void addChild(int index, String nodeName) {
		addObject(index, nodeName);
	}

	public void addChild(Ldom obj) {
		obj.setParent(this);
		setObject(obj.getNodeName(), obj);
	}

	public void addChild(int index, Ldom node) {
		addObject(index, node);
	}

	public void addBigBrother(Ldom bigBrother) {
		Ldom parent = getParent();
		if (parent == null)
			return;
		for (int i = 0; i < parent.size(); i++) {
			Ldom child = parent.get(i);
			if (!child.equals(this))
				continue;
			parent.addObject(i, bigBrother);
			break;
		}

	}

	public void setAllNode(Node node) {
		if (node == null) {
			return;
		}
		setValue(node);
		setAttributes(node);
		Ldom parent = getParent();
		int size = parent.size();
		if (size > 1) {
			Ldom previous = (Ldom) parent.getObject(size - 2);
			setPrevious(previous);
			previous.setNext(this);
		}
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			String name = child.getNodeName();
			if (!"#text".equals(name)) {
				Ldom obj = newChild(name);
				obj.setAllNode(child);
			}
		}

	}

	private void setValue(Node node) {
		nodeValue = "";
		NodeList valueList = node.getChildNodes();
		for (int i = 0; i < valueList.getLength(); i++) {
			Node child = valueList.item(i);
			String value = child.getNodeValue();
			if (value != null && !"".equals(value.trim())) {
				nodeValue += value;
			}

		}

	}

	public NamedNodeMap getAttributes(Node node) {
		return node.getAttributes();
	}

	public Attributes getAttributes() {
		if (node == null) {
			return null;
		}
		NamedNodeMap attributes = getAttributes(node);
		ArrayMap map = new ArrayMap(attributes.getLength());
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			Object attributeName = attribute.getNodeName();
			Object attributeValue = attribute.getNodeValue();
			map.put(attributeName, attributeValue);
		}
		return new AttributesImpl(map);
	}

	private void setAttributes(Node node) {
		if (node == null) {
			return;
		}
		this.node = node;
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) {
			return;
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			String name = attribute.getNodeName();
			String value = attribute.getNodeValue();
			set(name, value);
		}
	}

	public int getIndent() {
		List parents = getParents(null);
		return parents.size();
	}

	public int getIndent(String nodeName) {
		List parents = getParents(nodeName);
		return parents.size();
	}

	public DataObjectTable getDataObjectParents() {
		return getDataObjectParents(null);
	}

	public DataObjectTable getDataObjectParents(String node) {
		List list = getParents(node);
		return new DataObjectTableImpl(list);
	}

	public List getParents() {
		return getParents(null);
	}

	public List getParents(String nodeName) {
		List parents = new ArrayList();
		setParents(nodeName, this, parents);
		return parents;
	}

	private void setParents(String nodeName, Ldom node, List list) {
		Ldom parent = node.getParent();
		if (parent == null)
			return;
		String parentNodeName = parent.getNodeName();
		if (nodeName != null) {
			if (!nodeName.equals(parentNodeName))
				return;
			list.add(parent);
			setParents(nodeName, parent, list);
		} else {
			list.add(parent);
			setParents(nodeName, parent, list);
		}
	}

	public Ldom getAncestor(String nodeName) {
		List parents = getParents(nodeName);
		if (parents.size() == 0)
			return null;
		else
			return (Ldom) parents.get(parents.size() - 1);
	}

	private Ldom getParent() {
		return parent;
	}

	private void setParent(Ldom parent) {
		this.parent = parent;
	}

	public Ldom getPrevious() {
		return previous;
	}

	private void setPrevious(Ldom previous) {
		this.previous = previous;
	}

	public Ldom next() {
		if (iterator == null || iterator.hasNext()) {
			iterator = getChildren().iterator();
		}
		return (Ldom) iterator.next();
	}

	private void setNext(Ldom next) {
		this.next = next;
	}

	public Ldom getNext() {
		return next;
	}

	public void up() {
		Ldom parent = getParent();
		int size = parent.size();
		for (int i = 0; i < size; i++) {
			Ldom child = parent.get(i);
			if (!equals(child))
				continue;
			if (i > 0) {
				Ldom previous = parent.get(i - 1);
				parent.setObject(i, previous);
				parent.setObject(i - 1, this);
			}
			break;
		}

	}

	public void upFirst() {
		Ldom parent = getParent();
		if (parent == null)
			return;
		int size = parent.size();
		for (int i = 0; i < size; i++) {
			Ldom child = parent.get(i);
			if (!equals(child))
				continue;
			if (i > 0) {
				parent.removeObject(i);
				parent.addObject(0, this);
			}
			break;
		}

	}

	public void down() {
		Ldom parent = getParent();
		int size = parent.size();
		for (int i = 0; i < size; i++) {
			Ldom child = parent.get(i);
			if (!equals(child))
				continue;
			if (i < size - 1) {
				Ldom next = parent.get(i + 1);
				parent.setObject(i, next);
				parent.setObject(i + 1, this);
			}
			break;
		}

	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	public Object getEigenValue() {
		return eigenValue;
	}

	public void setEigenValue(Object eigenValue) {
		this.eigenValue = eigenValue;
	}

	public final void marge(Ldom target) {
		removeChildren();
		set(target);
		for (int i = 0; i < target.size(); i++) {
			Ldom child = target.get(i);
			addChild(child);
		}

	}

	public String getEncode() {
		return encoding;
	}

	public void setEncode(String encode) {
		encoding = encode;
	}

	public String toFirstHtml() {
		return xmlBuffer.toString();
	}

	public String toXml() {
		return toXml(LSystem.encoding);
	}

	public String toXml(String encode) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"" + encode + "\"?>" + LS);
		sb.append(LS);
		if (comment != null) {
			sb.append(comment);
			sb.append(LS);
		}
		sb.append(toChildXml(""));
		return sb.toString();
	}

	public String toHtml() {
		return toChildXml("", false, true);
	}

	private String toChildXml(String tab) {
		return toChildXml(tab, true, true);
	}

	private String toChildXml(String tab, boolean isEscape, boolean isClosed) {
		tab = tab + "\t";
		StringBuffer sb = new StringBuffer();
		sb.append(tab + "<" + nodeName);
		String keys[] = getValueKeys();
		for (int i = 0; i < keys.length; i++) {
			String value = get(keys[i]);
			if (value != null) {
				if (isEscape) {
					value = StringUtils.toFromXML(value);
				} else {
					value = StringUtils.replace(value, "&amp;", "&");
				}
				sb.append(" " + keys[i] + "=\"" + value + "\"");
			}
		}

		List children = getChildren();
		Object eigenValue = getEigenValue();
		if (children.size() != 0 || eigenValue != null) {
			sb.append(">" + LS);
		}
		if (eigenValue != null) {
			if ("".equals(eigenValue)) {
				sb.append("</" + getNodeName() + ">" + LS);
			} else {
				sb.append(tab + "\t" + eigenValue + LS);
				sb.append(tab + "</" + getNodeName() + ">" + LS);
			}
			return sb.toString();
		}
		for (int i = 0; i < children.size(); i++) {
			Ldom child = (Ldom) children.get(i);
			sb.append(child.toChildXml(tab, isEscape, isClosed));
		}

		if (nodeValue != null && nodeValue.length() != 0) {
			if (children.size() == 0)
				sb.append(">");
			sb.append(getNodeValue() + "</" + getNodeName() + ">" + LS);
			return sb.toString();
		}
		if (children.size() == 0) {
			if (isNodeClose()) {
				if (isClosed)
					sb.append("/>" + LS);
				else
					sb.append("></" + getNodeName() + ">" + LS);
			} else {
				sb.append("></" + getNodeName() + ">" + LS);
			}
		} else {
			sb.append(tab + "</" + nodeName + ">" + LS);
		}
		return sb.toString();
	}

	class Wait {
		private long startTime;

		private long stopTime;

		private String name;

		public Wait() {
			this("WAIT", true);
		}

		public Wait(Object instance) {
			this(instance, true);
		}

		public Wait(boolean show) {
			this(((String) (null)), show);
		}

		public Wait(String name) {
			this(name, true);
		}

		public Wait(String name, boolean show) {
			this.startTime = 0L;
			this.stopTime = 0L;
			start();
			setName(name);
		}

		public Wait(Object instance, boolean show) {
			this.startTime = 0L;
			this.stopTime = 0L;
			start();
			String name = instance.getClass().getName();
			int lastIndexOf = name.lastIndexOf('.');
			if (name.lastIndexOf('.') != -1)
				name = name.substring(lastIndexOf + 1);
			setName(name);
		}

		public void start() {
			this.startTime = System.currentTimeMillis();
			this.stopTime = 0L;

		}

		public void stop() {
			this.stopTime = System.currentTimeMillis();
		}

		public void reset() {
			this.startTime = 0L;
			this.stopTime = 0L;
		}

		public void suspend() {
			this.startTime += System.currentTimeMillis() - this.stopTime;
			this.stopTime = -1L;
		}

		public long getTime() {
			if (this.stopTime == 0L) {
				if (this.startTime == 0L)
					return 0L;
				else
					return System.currentTimeMillis() - this.startTime;
			} else {
				return this.stopTime - this.startTime;
			}
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String toString() {
			return Long.toString(getTime());
		}

	}
}
