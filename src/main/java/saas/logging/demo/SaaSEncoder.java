package saas.logging.demo;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class SaaSEncoder extends PatternLayoutEncoder {

	private static Map<String, Stack<String>> saasLogsMap = new ConcurrentHashMap<String, Stack<String>>();
	  
	public SaaSEncoder() {
		super();
	}

	@Override
	public void doEncode(ILoggingEvent event) throws IOException {
	    String logText = getLayout().doLayout(event);
	    String logKey = event.getThreadName() + "_" + event.getMDCPropertyMap().get("SUBSCRIBER_MDC_KEY");
	    Stack<String> logStack = saasLogsMap.get(logKey);
	    if (logStack == null) {
	    	logStack = new Stack<String>();
	    	saasLogsMap.put(logKey, logStack);
	    }
	    logStack.push(logText);
	}

	/**
	 * @return the saasLogsMap
	 */
	public static Map<String, Stack<String>> getSaasLogsMap() {
		return saasLogsMap;
	}
	
}
