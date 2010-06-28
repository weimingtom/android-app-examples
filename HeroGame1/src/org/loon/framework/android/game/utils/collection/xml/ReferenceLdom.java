package org.loon.framework.android.game.utils.collection.xml;

import java.util.ArrayList;
import java.util.List;

import org.loon.framework.android.game.utils.StringUtils;

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
abstract class ReferenceLdom extends DataObjectImpl implements
		ReferenceConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List result;

	public ReferenceLdom() {
		result = new ArrayList();
	}

	public ReferenceLdom(DataObject data) {
		super(data);
		result = new ArrayList();
	}

	public abstract String getNodeName();

	public abstract String getNodeValue();

	public abstract void setNodeValue(String s);

	public boolean isExist(String name, String value) {
		return isExist(null, name, value);
	}

	public boolean isExist(String node, String name, String value) {
		Ldom target = select(node, name, value);
		return target != null;
	}

	private Ldom getFirstResult(List result) {
		if (result.size() > 0)
			return (Ldom) result.get(0);
		else
			return null;
	}

	public void setAttributeValue(String targetValue, String value) {
		setAttributeValue(targetValue, value, ((List) (new ArrayList())));
	}

	public void setAttributeBindValue(String targetValue, String value) {
		setAttributeBindValue(targetValue, value, ((List) (new ArrayList())));
	}

	public void setAttributeBindValue(String targetValue, String value,
			List escapeNode) {
		setAttributeValue("${" + targetValue + "}", value, escapeNode);
	}

	public void setBindValue(DataObject params) {
		setBindValue(params, ((List) (new ArrayList())), true);
	}

	public void setBindValue(DataObject params, boolean isEscape) {
		setBindValue(params, ((List) (new ArrayList())), isEscape);
	}

	public void setBindValue(DataObject params, List escapeNode,
			boolean isEscape) {
		String thisKeys[] = getValueKeys();
		for (int i = 0; i < thisKeys.length; i++) {
			String thisName = thisKeys[i];
			String thisValue = get(thisName);
			if (thisValue != null && thisValue.indexOf("${") != -1) {
				String subValue = DataObjectUtil.getSubstituteValue(thisValue,
						params);
				set(thisName, subValue);
			}
		}
		// 遍历分隔符
		for (int i = 0; i < size(); i++) {
			Ldom child = get(i);
			String childNode = child.getNodeName();
			if (!escapeNode.contains(childNode)) {
				String childNodeValue = child.getNodeValue();
				if (childNodeValue != null
						&& childNodeValue.indexOf("${") != -1) {
					String subValue = DataObjectUtil.getSubstituteValue(
							childNodeValue, params, isEscape);
					child.setNodeValue(subValue);
				}
				child.setBindValue(params, escapeNode, isEscape);
			}
		}

	}

	public void setAttributeBindValue(DataObject data) {
		setAttributeBindValue(data, ((List) (new ArrayList())),
				((List) (new ArrayList())));
	}

	public void setAttributeBindValue(DataObject data, List escapeNode,
			List escapeAttribute) {
		String thisKeys[] = getValueKeys();
		for (int i = 0; i < thisKeys.length; i++) {
			String thisName = thisKeys[i];
			String thisValue = get(thisName);
			if (thisValue != null && thisValue.indexOf("${") != -1) {
				String key = thisValue.substring(2, thisValue.length() - 1);
				if (!escapeAttribute.contains(key)
						&& data.containsValueKey(key)) {
					String value = data.get(key);
					set(thisName, value);
				}
			}
		}

		for (int i = 0; i < size(); i++) {
			Ldom child = get(i);
			String childNode = child.getNodeName();
			if (!escapeNode.contains(childNode))
				child.setAttributeBindValue(data, escapeNode, escapeAttribute);
		}

	}

	public void setAttributeValue(String targetValue, String value,
			List escapeNode) {
		String thisKeys[] = getValueKeys();
		for (int i = 0; i < thisKeys.length; i++) {
			String thisName = thisKeys[i];
			String thisValue = get(thisName);
			if (thisValue != null && thisValue.indexOf(targetValue) != -1) {
				String target = StringUtils.replace(thisValue, targetValue,
						value);
				set(thisName, target);
			}
		}

		for (int i = 0; i < size(); i++) {
			Ldom child = get(i);
			String childNode = child.getNodeName();
			if (!escapeNode.contains(childNode)) {
				String keys[] = child.getValueKeys();
				for (int j = 0; j < keys.length; j++) {
					String childName = keys[j];
					String childValue = child.get(childName);
					if (childValue != null
							&& childValue.indexOf(targetValue) != -1) {
						String target = StringUtils.replace(childValue,
								targetValue, value);
						child.set(childName, target);
					}
				}

				child.setAttributeValue(targetValue, value, escapeNode);
			}
		}

	}

	public void setNodeValue(String targetValue, String value) {
		setNodeValue(targetValue, value, ((List) (new ArrayList())));
	}

	public void setNodeBindValue(String targetValue, String value) {
		setNodeBindValue(targetValue, value, ((List) (new ArrayList())));
	}

	public void setNodeBindValue(String targetValue, String value,
			List escapeNode) {
		setNodeValue("${" + targetValue + "}", value, escapeNode);
	}

	public void setNodeValue(String targetValue, String value, List escapeNode) {
		String nodeValue = getNodeValue();
		if (nodeValue != null && nodeValue.indexOf(targetValue) != -1) {
			String subValue = StringUtils
					.replace(nodeValue, targetValue, value);
			setNodeValue(subValue);
		}
		for (int i = 0; i < size(); i++) {
			Ldom child = get(i);
			String childNode = child.getNodeName();
			if (escapeNode.contains(childNode)) {
			} else {
				child.setNodeValue(targetValue, value, escapeNode);
			}
		}

	}

	public Ldom selectValue(String value) {
		return selectValue(value, "=");
	}

	public Ldom selectValue(String value, String sign) {
		List result = new ArrayList();
		selectValue(value, sign, result);
		return getFirstResult(result);
	}

	protected void selectValue(String value, String sign, List result) {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			Ldom child = (Ldom) children.get(i);
			Object values[] = child.getValues();
			for (int j = 0; j < values.length; j++) {
				String childValue = (String) values[j];
				if (childValue != null)
					if ("=".equals(sign)) {
						if (childValue.equals(value))
							result.add(child);
					} else if ("LIKE".equals(sign)
							&& childValue.indexOf(value) != -1)
						result.add(child);
			}

			child.selectValue(value, sign, result);
		}

	}

	public Ldom selectOne(String nodeName) {
		List list = new ArrayList();
		if (nodeName.equals(getNodeName())) {
			return (Ldom) this;
		} else {
			select(nodeName, list, true);
			result = list;
			return getFirstResult(list);
		}
	}

	public Ldom select(String nodeName) {
		List list = new ArrayList();
		if (nodeName.equals(getNodeName())) {
			list.add(this);
		}
		select(nodeName, list, false);
		result = list;
		return getFirstResult(list);
	}

	protected void select(String nodeName, List result, boolean isOne) {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			Ldom child = (Ldom) children.get(i);
			String childNodeName = child.getNodeName();
			if (nodeName.equals(childNodeName)) {
				result.add(child);
				if (isOne)
					return;
			}
			child.select(nodeName, result, isOne);
		}

	}

	public Ldom selectInclude(String key, String value) {
		return select(null, key, value);
	}

	public Ldom selectInclude(String nodeName, String key, String value) {
		Reference ref = new Reference();
		ref.add(key, "LIKE", value, "String");
		return select(nodeName, ref);
	}

	public Ldom select(Reference reference) {
		return select(null, reference);
	}

	public Ldom select(String nodeName, Reference reference) {
		List result = new ArrayList();
		boolean isHit = isHit(reference, this);
		if (isHit) {
			result.add(this);
		}
		select(nodeName, reference, result, false);
		this.result = result;
		return getFirstResult(result);
	}

	protected void select(String nodeName, Reference reference, List result,
			boolean isOne) {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			Ldom child = (Ldom) children.get(i);
			String childNodeName = child.getNodeName();
			if (reference.size() != 0) {
				if (nodeName == null) {
					boolean isHit = isHit(reference, child);
					if (isHit) {
						result.add(child);
						if (isOne)
							return;
					}
				} else if (nodeName.equals(childNodeName)) {
					boolean isHit = isHit(reference, child);
					if (isHit) {
						result.add(child);
						if (isOne)
							return;
					}
				}
				child.select(nodeName, reference, result, isOne);
			}
		}

	}

	private boolean isHit(Reference reference, ReferenceLdom child) {
		boolean isHit = true;
		for (int j = 0; j < reference.size(); j++) {
			String key = reference.getColumnName(j);
			String sign = reference.getSign(j);
			String value = reference.getColumnValue(j);
			String condition = reference.getCondition(j);
			String childValue = child.get(key);
			if (childValue == null) {
				if (!"OR".equals(condition))
					isHit = false;
			} else if (value == null)
				isHit = false;
			else if ("=".equals(sign)) {
				if (!childValue.equals(value) && !"OR".equals(condition))
					isHit = false;
			} else if ("LIKE".equals(sign))
				if (childValue.indexOf(value) == -1) {
					if (!"OR".equals(condition))
						isHit = false;
				} else if ("OR".equals(condition))
					return true;
		}

		return isHit;
	}

	public Ldom select(String name, String value) {
		return select(null, name, value, "=", false);
	}

	public Ldom select(String nodeName, String name, String value) {
		return select(nodeName, name, value, "=", true);
	}

	public Ldom selectAll(String nodeName, String name, String value) {
		return select(nodeName, name, value, "=", false);
	}

	public Ldom select(String nodeName, String name, String value, String type,
			boolean isOne) {
		List result = new ArrayList();
		Reference ref = new Reference();
		ref.add(name, type, value, "String");
		boolean isHit = isHit(ref, this);
		if (isHit) {
			result.add(this);
			if (isOne) {
				this.result = result;
				return (Ldom) this;
			}
		}
		select(nodeName, ref, result, isOne);
		if (!isOne) {
			this.result = result;
		}
		return getFirstResult(result);
	}

	public void deleteOne(String nodeName) {
		delete(nodeName, true);
	}

	public void delete(String nodeName) {
		delete(nodeName, false);
	}

	public void delete(String nodeName, boolean isOne) {
		List children = getChildren();
		List removeObj = new ArrayList();
		for (int i = 0; i < children.size(); i++) {
			Ldom child = (Ldom) children.get(i);
			String childNodeName = child.getNodeName();
			if (nodeName.equals(childNodeName)) {
				removeObj.add(child);
				if (isOne)
					break;
			} else {
				child.delete(nodeName);
			}
		}

		for (int i = 0; i < removeObj.size(); i++)
			removeChild(removeObj.get(i));

	}

	public void delete(String key, String value) {
		delete(null, key, value);
	}

	public void delete(String nodeName, String key, String value) {
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			Ldom child = (Ldom) children.get(i);
			String childValue = child.get(key);
			if (childValue != null) {
				String childNodeName = child.getNodeName();
				if (nodeName == null) {
					if (childValue.equals(value)) {
						removeChild(child);
					}
				} else if (nodeName.equals(childNodeName)
						&& childValue.equals(value))
					removeChild(child);
			}
			child.delete(nodeName, key, value);
		}

	}

	public List getResult() {
		return result;
	}

	public Ldom getResult(int index) {
		return (Ldom) result.get(index);
	}

	public int resultSize() {
		return result.size();
	}

	public int size() {
		return objectSize();
	}

	public Ldom get(int index) {
		return (Ldom) getObject(index);
	}

	public Ldom getFirstChild() {
		if (objectSize() == 0) {
			return null;
		} else {
			return (Ldom) getObject(0);
		}
	}

	public Ldom getChild(String key) {
		return (Ldom) getObject(key);
	}

	public void removeChild(int index) {
		removeObject(index);
	}

	public boolean removeChild(Object value) {
		return removeObject(value);
	}

	public void removeChildren() {
		removeObjectAll();
	}

	public List getChildren() {
		return getObjects();
	}

	public boolean isChildren(String key) {
		return containsObjectKey(key);
	}

}
