package saas.logging.demo;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SaaSLoggingServiceImpl - implementation of SaaSLoggingService. 
 * 
 */
@WebService(endpointInterface = "saas.logging.demo.SaaSLoggingService")
@HandlerChain(file = "../../jaxws-handlers.xml")
public class SaaSLoggingServiceImpl implements SaaSLoggingService {

	private static final Logger LOG = LoggerFactory.getLogger(SaaSLoggingServiceImpl.class);
	
	@Resource
	private WebServiceContext wsc;
	
	/**
	 * SaaSLoggingServiceImpl - Default Constructor.
	 */
	public SaaSLoggingServiceImpl() {
		super();
	}


	@Override
	public String fetchGreeting(final String personName, final String subscriberName) {
		LOG.debug("personName:{}.", personName);
		//get Document Model of omApplication xml
		MessageContext context = this.wsc.getMessageContext();
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("personName:{}.", personName);
	    return "Hello " + personName;
	}

}
