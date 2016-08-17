package za.co.invictus.shoprite.javamapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

import za.co.invictus.shoprite.FlattenPriceGroup;
import za.co.invictus.shoprite.PriceGroup;

public class PriceGroupJavaMapping extends AbstractTransformation {

	@Override
	public void transform(TransformationInput input, TransformationOutput output)
			throws StreamTransformationException {

	}

	public void execute(InputStream in, OutputStream out) throws ParseException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(in);

			ArrayList<ArrayList<PriceGroup>> flattenPriceGroup=flattenPricing(doc);
			deleteE1WBB07(doc);
			addFlattenE1WBB07(doc, flattenPriceGroup);
			doc.normalizeDocument();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<PriceGroup>> flattenPricing(Document doc) throws DOMException,
			ParseException {
		ArrayList<ArrayList<PriceGroup>> flattentPriceGroupPerE1WBB03 = new ArrayList<ArrayList<PriceGroup>>();
		NodeList E1WBB03List = doc.getElementsByTagName("E1WBB03");
		PriceGroup priceGroup = null;
		for (int i = 0; i < E1WBB03List.getLength(); i++) {
			Node E1WBB03 = E1WBB03List.item(i);
			NodeList E1WBB03Children = E1WBB03.getChildNodes();
			FlattenPriceGroup flattenPG = new FlattenPriceGroup();
			for (int j = 0; j < E1WBB03Children.getLength(); j++) {
				Node E1WBB03Child = E1WBB03Children.item(j);
				if (E1WBB03Child.getNodeName().equals("E1WBB07")) {
					NodeList E1WBB07Children = E1WBB03Child.getChildNodes();
					for (int k = 0; k < E1WBB07Children.getLength(); k++) {
						Node node = E1WBB07Children.item(k);

						if (node.getNodeType() == Node.ELEMENT_NODE) {

							if (node.getNodeName() == "KSCHL") {
								priceGroup = new PriceGroup();
								// System.out.println(node.getTextContent());
								priceGroup.setType(node.getTextContent());
							}
							if (node.getNodeName() == "DATAB") {
								priceGroup.setStartDate(formatDate(node
										.getTextContent()));
							}
							if (node.getNodeName() == "DATBI") {
								// System.out.println(node.getTextContent());
								priceGroup.setEndDate(formatDate(node
										.getTextContent()));
							}
							if (node.getNodeName() == "AKTNR") {
								priceGroup.setPromotionId((node
										.getTextContent()));
							}

						} else {
							if (node.getNodeName() == "E1WBB08") {
								NodeList E1WBB08NodeList = node.getChildNodes();
								if (E1WBB08NodeList.getLength() > 0) {
									Node E1WBB08 = E1WBB08NodeList.item(0);
									NodeList E1WBB08Children = E1WBB08
											.getChildNodes();
									for (int m = 0; m < E1WBB08Children
											.getLength(); m++) {
										if (E1WBB08Children.item(k)
												.getNodeName() == "KWERT") {
											if (E1WBB08Children.item(k)
													.getTextContent() != null
													&& E1WBB08Children.item(k)
															.getTextContent()
															.length() > 0) {
												priceGroup
														.setPrice(Double
																.parseDouble(E1WBB08Children
																		.item(k)
																		.getTextContent()));
											}
										}
									}
								}

							}
						}

					}
					if (priceGroup != null) {
						flattenPG.addPriceGroup(priceGroup);
					}
				}
			}

			ArrayList<PriceGroup> flattenPriceGroup = flattenPG
					.flatneenPriceGroup();

			System.out.println("**************START*****************");
			for (PriceGroup pg : flattenPriceGroup) {

				System.out.println("Type	" + pg.getType());
				System.out.println("Start Date	"
						+ formatDate(pg.getStartDate()));
				System.out.println("End Date	" + formatDate(pg.getEndDate()));
				System.out.println("Price	" + pg.getPrice());
				System.out.println("Promotion ID	" + pg.getPromotionId());

			}
			System.out.println("**************END*****************");
			flattentPriceGroupPerE1WBB03.add(flattenPriceGroup);
		}
		return flattentPriceGroupPerE1WBB03;
		
	}

	public ArrayList<ArrayList<Node>> getArrayListtoFlatten(
			NodeList E1WBB03NodeList) {
		ArrayList<ArrayList<Node>> E1WBB03GroupingList = new ArrayList<ArrayList<Node>>();
		try {

			for (int i = 0; i < E1WBB03NodeList.getLength(); i++) {
				NodeList E1WBB03Children = E1WBB03NodeList.item(i)
						.getChildNodes();
				ArrayList<Node> nodeList = null;
				for (int j = 0; j < E1WBB03Children.getLength(); j++) {
					Node node = E1WBB03Children.item(j);
					if (node.getNodeName() == "E1WBB07") {
						nodeList = new ArrayList<Node>();
						// System.out.println(E1WBB03Children.item(j));
						nodeList.add(E1WBB03Children.item(j));
					}
				}
				if (nodeList != null) {
					E1WBB03GroupingList.add(nodeList);
					nodeList = null;
				}

			}

		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return E1WBB03GroupingList;
	}

	public ArrayList<ArrayList<PriceGroup>> flattenE1WBB07(
			ArrayList<ArrayList<Node>> nodeList) throws DOMException,
			ParseException {
		ArrayList<ArrayList<PriceGroup>> list = new ArrayList<ArrayList<PriceGroup>>();
		for (int i = 0; i < nodeList.size(); i++) {
			ArrayList<PriceGroup> pgArrayList = new ArrayList<PriceGroup>();
			ArrayList<Node> E1WBB07List = nodeList.get(i);

			for (int j = 0; j < E1WBB07List.size(); j++) {
				PriceGroup priceGroup = new PriceGroup();
				Node node = E1WBB07List.get(j);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					if (node.getNodeName() == "KSCHL") {
						priceGroup.setType(node.getTextContent());
					}
					if (node.getNodeName() == "DATAB") {
						priceGroup.setStartDate(formatDate(node
								.getTextContent()));
					}
					if (node.getNodeName() == "DATBI") {
						System.out.println(node.getTextContent());
						priceGroup
								.setEndDate(formatDate(node.getTextContent()));
					}
					if (node.getNodeName() == "AKTNR") {
						priceGroup.setPromotionId((node.getTextContent()));
					}

				} else {
					if (node.getNodeName() == "E1WBB08") {
						NodeList E1WBB08NodeList = node.getChildNodes();
						if (E1WBB08NodeList.getLength() > 0) {
							Node E1WBB08 = E1WBB08NodeList.item(0);
							NodeList E1WBB08Children = E1WBB08.getChildNodes();
							for (int k = 0; k < E1WBB08Children.getLength(); k++) {
								if (E1WBB08Children.item(k).getNodeName() == "KWERT") {
									if (E1WBB08Children.item(k)
											.getTextContent() != null
											&& E1WBB08Children.item(k)
													.getTextContent().length() > 0) {
										priceGroup.setPrice(Double
												.parseDouble(E1WBB08Children
														.item(k)
														.getTextContent()));
									}
								}
							}
						}

					}
				}

				if (priceGroup.getType().equals("VKAO")
						|| priceGroup.getType().equals("VPAO")) {
					pgArrayList.add(priceGroup);
				}
			}
			list.add(pgArrayList);
		}

		return list;

	}

	private void addFlattenE1WBB07(Document doc,
			ArrayList<ArrayList<PriceGroup>> alPriceGroup) {
		
		NodeList E1WBB03NodeList=doc.getElementsByTagName("E1WBB03");

		for (int i = 0; i < E1WBB03NodeList.getLength(); i++) {
			Node E1WBB03 = E1WBB03NodeList.item(i);
			ArrayList<PriceGroup> pgList = alPriceGroup.get(i);
			System.out.println(pgList.size());
			for (PriceGroup pg : pgList) {

				Element E1WBB07 = doc.createElement("E1WBB07");
		
				E1WBB03.appendChild(E1WBB07);

				Element KSCHL = doc.createElement("KSCHL");
				KSCHL.setTextContent(pg.getType());
				E1WBB07.appendChild(KSCHL);

				Element DATAB = doc.createElement("DATAB");
				DATAB.setTextContent(formatDate(pg.getStartDate()));
				E1WBB07.appendChild(DATAB);

				Element DATBI = doc.createElement("DATBI");
				DATBI.setTextContent(formatDate(pg.getEndDate()));
				E1WBB07.appendChild(DATBI);

				Element AKTNR = doc.createElement("AKTNR");
				AKTNR.setTextContent(pg.getPromotionId());
				E1WBB07.appendChild(AKTNR);

				Element E1WBB08 = doc.createElement("E1WBB08");
				E1WBB07.appendChild(E1WBB08);

				Element KWERT = doc.createElement("KWERT");
				KWERT.setTextContent(new Double(pg.getPrice()).toString());
				E1WBB07.appendChild(KWERT);
				E1WBB03.appendChild(E1WBB07);

			}
		}

	}

	private void deleteE1WBB07(Document doc) {
		NodeList nodeList = doc.getElementsByTagName("E1WBB07");
		System.out.println(nodeList.getLength());
		for (int i = nodeList.getLength()-1; i >= 0; i--) {
			Node E1WBB07 = nodeList.item(i);
			Node E1WBB03 = E1WBB07.getParentNode();
			E1WBB03.removeChild(E1WBB07);
			E1WBB03.normalize();
		}

	}

	private static Date formatDate(String inputDate) throws ParseException {

		DateFormat inDateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat outDateFormat = new SimpleDateFormat("yyyy/MM/dd");

		Calendar cal = Calendar.getInstance();
		Date indate = null;
		String outdate = null;
		try {
			indate = inDateFormat.parse(inputDate);
			cal.setTime(indate);
			cal.add(Calendar.DATE, 0);
			outdate = outDateFormat.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outDateFormat.parse(outdate);
	}

	private static String formatDate(Date date) {
		String outdate = null;

		DateFormat outDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 0);
		outdate = outDateFormat.format(cal.getTime());

		return outdate;
	}

}
