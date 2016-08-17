package za.co.invictus.shoprite.javamapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

import java.util.ArrayList;
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

import za.co.invictus.shoprite.DateFormat;
import za.co.invictus.shoprite.FlattenPriceGroup;
import za.co.invictus.shoprite.PriceGroup;

public class PriceGroupJavaMapping extends AbstractTransformation {

	@Override
	public void transform(TransformationInput input, TransformationOutput output)
			throws StreamTransformationException {

		try {
			execute(input.getInputPayload().getInputStream(), output
					.getOutputPayload().getOutputStream());
		} catch (MerchandiseException e) {
			new StreamTransformationException(e.getMessage());
		}

	}

	public void execute(InputStream in, OutputStream out)
			throws MerchandiseException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(in);

			ArrayList<ArrayList<PriceGroup>> flattenPriceGroup = flattenPricing(doc);
			deleteE1WBB07(doc);
			addFlattenE1WBB07(doc, flattenPriceGroup);
			doc.normalizeDocument();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			throw new MerchandiseException(e.getMessage());
		} catch (SAXException e) {
			throw new MerchandiseException(e.getMessage());
		} catch (IOException e) {
			throw new MerchandiseException(e.getMessage());
		} catch (DOMException e) {
			throw new MerchandiseException(e.getMessage());
		} catch (TransformerConfigurationException e) {
			throw new MerchandiseException(e.getMessage());
		} catch (TransformerException e) {
			throw new MerchandiseException(e.getMessage());
		} catch (ParseException e) {
			throw new MerchandiseException(e.getMessage());
		}
	}

	public ArrayList<ArrayList<PriceGroup>> flattenPricing(Document doc)
			throws DOMException, MerchandiseException, ParseException {
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
								priceGroup.setStartDate(DateFormat.formatDate(node
										.getTextContent()));
							}
							if (node.getNodeName() == "DATBI") {
								// System.out.println(node.getTextContent());
								priceGroup.setEndDate(DateFormat.formatDate(node
										.getTextContent()));
							}
							if (node.getNodeName() == "AKTNR") {
								priceGroup.setPromotionId((node
										.getTextContent()));
							}

							if (node.getNodeName() == "E1WBB08") {
								NodeList E1WBB08NodeList = node.getChildNodes();
								for (int n = 0; n < E1WBB08NodeList.getLength(); n++) {
									Node E1WBB08 = E1WBB08NodeList.item(n);

									if (E1WBB08.getNodeName().equals("KWERT")) {
										priceGroup.setPrice(Double
												.parseDouble(E1WBB08
														.getTextContent()));
									}
								}
							}

						} else {

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
						+ DateFormat.formatDate(pg.getStartDate()));
				System.out.println("End Date	" + DateFormat.formatDate(pg.getEndDate()));
				System.out.println("Price	" + pg.getPrice());
				System.out.println("Promotion ID	" + pg.getPromotionId());

			}
			System.out.println("**************END*****************");
			flattentPriceGroupPerE1WBB03.add(flattenPriceGroup);
		}
		return flattentPriceGroupPerE1WBB03;

	}

	private void addFlattenE1WBB07(Document doc,
			ArrayList<ArrayList<PriceGroup>> alPriceGroup) {

		NodeList E1WBB03NodeList = doc.getElementsByTagName("E1WBB03");

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
				DATAB.setTextContent(DateFormat.formatDate(pg.getStartDate()));
				E1WBB07.appendChild(DATAB);

				Element DATBI = doc.createElement("DATBI");
				DATBI.setTextContent(DateFormat.formatDate(pg.getEndDate()));
				E1WBB07.appendChild(DATBI);

				Element AKTNR = doc.createElement("AKTNR");
				AKTNR.setTextContent(pg.getPromotionId());
				E1WBB07.appendChild(AKTNR);

				Element E1WBB08 = doc.createElement("E1WBB08");

				Element KWERT = doc.createElement("KWERT");
				KWERT.setTextContent(new Double(pg.getPrice()).toString());
				E1WBB08.appendChild(KWERT);
				E1WBB07.appendChild(E1WBB08);
				E1WBB03.appendChild(E1WBB07);

			}
		}

	}

	private void deleteE1WBB07(Document doc) {
		NodeList nodeList = doc.getElementsByTagName("E1WBB07");
		System.out.println(nodeList.getLength());
		for (int i = nodeList.getLength() - 1; i >= 0; i--) {
			Node E1WBB07 = nodeList.item(i);
			Node E1WBB03 = E1WBB07.getParentNode();
			E1WBB03.removeChild(E1WBB07);
			E1WBB03.normalize();
		}

	}

	
}
