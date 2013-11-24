package saas.logging.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;

import org.slf4j.MDC;

import saas.logging.demo.jaxws.FetchGreeting;
import saas.logging.demo.jaxws.FetchGreetingResponse;
import saas.logging.demo.object.Logs;
import saas.logging.demo.object.RequestObject;
import saas.logging.demo.object.ResponseObject;

/**
 * JaxwsLogicalHandler - This class is a Utility Web Service Handler which
 * validates the input parameters.
 */

//TODO: handle exception

public class JaxwsLogicalHandler implements
		LogicalHandler<LogicalMessageContext> {

	private static Map<String, JAXBContext> jaxbContexts = new HashMap<String, JAXBContext>();
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
		Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		if (!outboundProperty.booleanValue()) {
			Boolean retrieveLogs = false;
			String subscriber = null;
			LogicalMessage lm = messageContext.getMessage();
			FetchGreeting fetchGreeting = (FetchGreeting)lm.getPayload(getJaxbContext(FetchGreeting.class));

			if (fetchGreeting != null) {
				RequestObject requestObject = fetchGreeting.getRequestObject();
				if (requestObject != null) {
					subscriber = requestObject.getSubscriberName();
					retrieveLogs = requestObject.isRetrieveLogs();
				}
			}
			// set MDC for logging
			MDC.put("SUBSCRIBER_MDC_KEY", subscriber);
			
	        messageContext.put("retrieveLogs", retrieveLogs);
			messageContext.setScope("retrieveLogs", Scope.HANDLER);
		}
		
		if (outboundProperty.booleanValue()) {
			Boolean retrieveLogs = (Boolean)messageContext.get("retrieveLogs");		
			String logKey = Thread.currentThread().getName() + "_" + MDC.get("SUBSCRIBER_MDC_KEY");
			Map<String, Stack<String>> saasLogsMap = SaaSEncoder.getSaasLogsMap();
			Stack<String> logStack = saasLogsMap.get(logKey);
			if (logStack != null) {
				if (retrieveLogs) {
					LogicalMessage lm = messageContext.getMessage();
					FetchGreetingResponse fetchGreetingResponse = (FetchGreetingResponse)lm.getPayload(getJaxbContext(FetchGreetingResponse.class));
					List<String> logEntry = null;
					if (fetchGreetingResponse != null) {
						ResponseObject responseObject = fetchGreetingResponse.getResponseObject();
						if (responseObject != null) {
							Logs logs = responseObject.getLogs();
							if (logs == null) {
								logs = new Logs();
								responseObject.setLogs(logs);
							}
							logEntry = logs.getLogEntry();
							if (logEntry == null) {
								logEntry = new ArrayList<String>();
							}
						}
					}
					while (!logStack.empty()){
						logEntry.add(logStack.pop());
					}
					lm.setPayload(fetchGreetingResponse, getJaxbContext(FetchGreetingResponse.class));
				} else {
					//empty the stack
					logStack.clear();
				}
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

	/**
	 * getJaxbContext - returns the cached JAXBContext for a class.
	 * 
	 * @param jaxbContextClass Class
	 * @return JAXBContext
	 */
	private static JAXBContext getJaxbContext(Class<?> jaxbContextClass) {
		if (jaxbContexts.get(jaxbContextClass.getName()) == null) {
			try {
				jaxbContexts.put(jaxbContextClass.getName(),
						JAXBContext.newInstance(jaxbContextClass));
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return jaxbContexts.get(jaxbContextClass.getName());
	}
}
