package saas.logging.demo;

import java.util.Map;
import java.util.Stack;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.MDC;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

		if (!outboundProperty.booleanValue()) {

			LogicalMessage lm = messageContext.getMessage();
			Source payload = lm.getPayload();

			// Process Payload Source
			String subscriber = null;

			if (payload != null) {
				Node docNode = getNodeFromSource(payload);
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
						}
					}
				}
			}
			// set MDC for logging
			MDC.put("SUBSCRIBER_MDC_KEY", subscriber);
		}
		
		if (outboundProperty.booleanValue()) {
			String logKey = Thread.currentThread().getName() + "_" + MDC.get("SUBSCRIBER_MDC_KEY");
			System.out.println(logKey);
			Map<String, Stack<String>> saasLogsMap = SaaSEncoder.getSaasLogsMap();
			Stack<String> logStack = saasLogsMap.get(logKey);
			System.out.println("returning");
			for (String string : logStack) {
				System.out.println(string);
			}
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
}
