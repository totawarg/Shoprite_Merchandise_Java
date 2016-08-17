package za.co.invictus.shoprite.xsl;

import java.io.Serializable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import za.co.invictus.shoprite.PriceGroup;

public class PriceGroupXML implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static String x=null; 
	
	public static String flattenPriceGroup(Object o){
		return "<Hello/>";
		
	}
	
	
	
	public static String getFirstLevelTextContent(Node node) {
		NodeList list = node.getChildNodes();
		StringBuilder textContent = new StringBuilder();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				textContent.append(child.getTextContent());
		}
		return textContent.toString();
	}
	
}
