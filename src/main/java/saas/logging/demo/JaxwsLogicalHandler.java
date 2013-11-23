package saas.logging.demo;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Stack;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.MDC;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import saas.logging.demo.response.ServiceResponse;

/**
 * JaxwsLogicalHandler - This class is a Utility Web Service Handler which
 * validates the input parameters.
 * 
 * 
 */
public class JaxwsLogicalHandler implements
		LogicalHandler<LogicalMessageContext> {

	/**
	 * Default Constructor.
	 */
	public JaxwsLogicalHandler() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.
	 * MessageContext)
	 */
	@Override
	public boolean handleMessage(LogicalMessageContext messageContext) {
		Boolean outboundProperty = (Boolean) messageContext
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		Boolean retrieveLogs = false;
		
		if (!outboundProperty.booleanValue()) {

			LogicalMessage lm = messageContext.getMessage();
			Source payload = lm.getPayload();

			// Process Payload Source
			String subscriber = null;
			
			if (payload != null) {
				Node docNode = getNodeFromSource(payload);
				//System.out.println(nodeToString(docNode));
				if (docNode != null) {
					Node rootNode = docNode.getFirstChild();
					if (rootNode != null) {
						NodeList nodeList = rootNode.getChildNodes();
						for (int counter = 0; counter < nodeList.getLength(); counter++) {
							Node childNode = nodeList.item(counter);
							if (childNode.getNodeName()
									.equals("subscriberName")) {
								subscriber = childNode.getTextContent();
								continue;
							}
							
							if (childNode.getNodeName()
									.equals("retrieveLogs")) {
								retrieveLogs = Boolean.parseBoolean(childNode.getTextContent());
								continue;
							}
						}
					}
				}
			}
			// set MDC for logging
			MDC.put("SUBSCRIBER_MDC_KEY", subscriber);
		}
		
		if (outboundProperty.booleanValue()) {
			
			LogicalMessage lm = messageContext.getMessage();
			Source payload = lm.getPayload();
			
			// TODO: Need to check how to get request parameter here.
			/*if (!retrieveLogs) {
				return true;
			}*/
			
			String logKey = Thread.currentThread().getName() + "_" + MDC.get("SUBSCRIBER_MDC_KEY");
			//System.out.println(logKey);
			Map<String, Stack<String>> saasLogsMap = SaaSEncoder.getSaasLogsMap();
			Stack<String> logStack = saasLogsMap.get(logKey);
			//System.out.println("returning");

			// Convert the payload to Document
			Document document = getDocumentFromSource(payload);
			// Get the ServiceResponse Element from the response payload
			Element serviceResponseElement = (Element) document.getFirstChild().getFirstChild();
			
			// Unmarshal the Element to JAXB Object of ServiceResponse
			Unmarshaller unmarshaller = null;
			ServiceResponse serviceResponse = null;
			try {
				unmarshaller = JAXBContext.newInstance(ServiceResponse.class).createUnmarshaller();
				serviceResponse = (ServiceResponse) unmarshaller.unmarshal(serviceResponseElement);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			for (String log : logStack) {
				serviceResponse.getLogs().getLogEntry().add(log);
			}
			
			// Marshalling the ServiceResponse object which has the logs back to the document
			
			Element parent = (Element) document.getFirstChild();
			parent.removeChild(serviceResponseElement);
			 
			try{
				 Marshaller marshaller = JAXBContext.newInstance(ServiceResponse.class).createMarshaller();
				 marshaller.marshal(serviceResponse, parent);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		
			DOMSource domSource = new DOMSource(document);
			lm.setPayload(domSource);
		
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext
	 * )
	 */
	@Override
	public boolean handleFault(LogicalMessageContext messageContext) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext messageContext) {
	}

	private static Document getDocumentFromSource(Source source) {
		return (Document) getNodeFromSource(source);
	}
	
	private static Node getNodeFromSource(Source source) {
		Node node = null;
		try {
			DOMResult dom = new DOMResult();
			Transformer trans = TransformerFactory.newInstance()
					.newTransformer();
			trans.transform(source, dom);
			node = dom.getNode();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return node;
	}
	
	/**
	 * nodeToString - converts the node into string.
	 * 
	 * @param Node node
	 * @return String
	 */
	public static String nodeToString(Node node) {
		String xmlString = null;
		if (node != null) {
			Writer writer = null;
			try {
				// initialize StreamResult with File object to save to file
				StreamResult result = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(node);
				
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                
				transformer.transform(source, result);
				writer = result.getWriter();
				xmlString = writer.toString();
				
			} catch (TransformerException e) {
				throw new RuntimeException(e);
			} 
		}
		return xmlString;
	}

}
