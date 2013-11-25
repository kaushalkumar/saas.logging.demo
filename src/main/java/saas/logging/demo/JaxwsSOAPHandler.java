package saas.logging.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.MDC;

import saas.logging.demo.object.Logs;


/**
 * JaxwsSOAPHandler - Handler to add logs along with soap fault.
 */
public class JaxwsSOAPHandler implements SOAPHandler<SOAPMessageContext> {

	
	/**
	 * Default Constructor.
	 */
	public JaxwsSOAPHandler() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext messageContext) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(SOAPMessageContext messageContext) {
		Boolean retrieveLogs = (Boolean)messageContext.get("retrieveLogs");		
		String logKey = Thread.currentThread().getName() + "_" + MDC.get("SUBSCRIBER_MDC_KEY");
		Map<String, Stack<String>> saasLogsMap = SaaSEncoder.getSaasLogsMap();
		Stack<String> logStack = saasLogsMap.get(logKey);
		if (logStack != null) {
			if (retrieveLogs) {
				//preparing logs
				List<String> logEntry = null;
				Logs logs = new Logs();
				logEntry = logs.getLogEntry();
				if (logEntry == null) {
					logEntry = new ArrayList<String>();
					logs.setLogEntry(logEntry);
				}
				while (!logStack.empty()){
					logEntry.add(logStack.pop());
				}
				
				//associating it to SOAP Message
				SOAPMessage soapmsg = messageContext.getMessage();
				try {
					SOAPFactory soapFactory = SOAPFactory.newInstance();
					SOAPElement logsElement = soapFactory.createElement(SaaSLoggingUtil.convertLogsToDocument(logs).getDocumentElement());
					if (soapmsg != null) {
						SOAPBody soapBody = soapmsg.getSOAPBody();
						if (soapBody != null) {
							SOAPFault soapFault = soapBody.getFault();
							if (soapFault != null) {
								Detail detail = soapFault.getDetail();
								if (detail == null) {
									detail = soapFault.addDetail();
									detail.addChildElement(logsElement);
								}
							}
						}
					}
				} catch (SOAPException e) {
					e.printStackTrace();
				}
			} else {
				//empty the stack
				logStack.clear();
			}
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext messageContext) {
	}
	
	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
