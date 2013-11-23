package saas.logging.demo;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saas.logging.demo.response.ServiceResponse;

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
	public ServiceResponse fetchGreeting(final String personName, final String subscriberName, 
			final String retrieveLogs) {
		LOG.debug("Entry personName:{}.", personName);
		//get Document Model of omApplication xml
		MessageContext context = this.wsc.getMessageContext();
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String result = "Hello " + personName;
		LOG.info("result:{}.", result);
		LOG.info("Exit personName:{}.", personName);
		ServiceResponse response = new ServiceResponse();
		response.setResponse(result);
		return response;
	}

}
