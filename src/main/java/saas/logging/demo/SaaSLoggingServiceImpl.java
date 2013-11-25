package saas.logging.demo;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saas.logging.demo.object.RequestObject;
import saas.logging.demo.object.ResponseObject;

/**
 * SaaSLoggingServiceImpl - implementation of SaaSLoggingService. 
 * 
 */
@WebService(endpointInterface = "saas.logging.demo.SaaSLoggingService")
@HandlerChain(file = "../../jaxws-handlers.xml")
public class SaaSLoggingServiceImpl implements SaaSLoggingService {

	private static final Logger LOG = LoggerFactory.getLogger(SaaSLoggingServiceImpl.class);
	
	/**
	 * SaaSLoggingServiceImpl - Default Constructor.
	 */
	public SaaSLoggingServiceImpl() {
		super();
	}


	@Override
	public ResponseObject fetchGreeting(RequestObject requestObject) {
		LOG.debug("Entry personName:{}.", requestObject.getPersonName());
		//get Document Model of omApplication xml
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if("".equals(requestObject.getPersonName())){
			throw new IllegalArgumentException("Invalid Parameter");
		}
		String result = "Hello " + requestObject.getPersonName();
		LOG.info("result:{}.", result);
		LOG.info("Exit personName:{}.", requestObject.getPersonName());
		ResponseObject responseObject = new ResponseObject();
		responseObject.setGreeting(result);
		return responseObject;
	}

}
