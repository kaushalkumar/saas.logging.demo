package saas.logging.demo;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import saas.logging.demo.response.ServiceResponse;


@WebService(targetNamespace = "http://demo.logging.saas/")
public interface SaaSLoggingService {

	@WebResult(name = "serviceResponse")
	ServiceResponse fetchGreeting(@XmlElement(required = true) @WebParam(name = "personName") String personName,
			@XmlElement(required = true) @WebParam(name = "subscriberName") String subscriberName,
			@WebParam(name = "retrieveLogs") String retrieveLogs);
	
	
}
