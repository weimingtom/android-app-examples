package org.loon.framework.android.game.utils.collection.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
 * @version 0.1.1
 */
public class XmlFile {

	private String fileName;

	public XmlFile() {
		this(null);
	}

	public XmlFile(String fileName) {
		this.fileName = fileName;
	}

	public Ldom makeInstance(String nodeName) {
		return new Ldom(nodeName);
	}

	public Ldom parse() throws ParserConfigurationException, IOException,
			SAXException {
		return parse(null);
	}

	/**
	 * 解析并获得指定节点在XML中的实例
	 * 
	 * @param firstNodeName
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public Ldom parse(String firstNodeName)
			throws ParserConfigurationException, IOException, SAXException {
		return parse(firstNodeName, Resources.getResourceAsStream(fileName));
	}

	/**
	 * 解析并获得指定节点在指定XML流中的实例
	 * 
	 * @param firstNodeName
	 * @param stream
	 * @return
	 * @throws ParserConfigurationException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws IOException
	 * @throws SAXException
	 */
	public Ldom parse(String firstNodeName, InputStream is)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		builder = factory.newDocumentBuilder();
		Document document = builder.parse(is);
		Node baseNode = getNode(document, null);
		Ldom objects = new Ldom(baseNode);
		NodeList nodeList = baseNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == 1) {
				String name = node.getNodeName();
				if (!"#text".equals(name) && name != null) {
					Ldom child = new Ldom(name, objects);
					child.setAllNode(node);
				}
			}
		}
		if (!StringUtils.isEmpty(firstNodeName)) {
			objects = objects.selectOne(firstNodeName);
		}
		return objects;
	}

	/**
	 * 获得指定节点
	 * 
	 * @param parentNode
	 * @param name
	 * @return
	 */
	private Node getNode(Node parentNode, String name) {
		NodeList nodeList = parentNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == 1) {
				// 不取空
				if (name == null) {
					return node;
				}
				String nodeName = node.getNodeName();
				if (name.equals(nodeName)) {
					return node;
				}
			}
		}
		return parentNode;
	}

}
