package saas.logging.demo;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;


@WebService(targetNamespace = "http://demo.logging.saas/")
public interface SaaSLoggingService {

	@WebResult(name = "greeting")
	String fetchGreeting(@XmlElement(required = true) @WebParam(name = "personName") String personName,
			@XmlElement(required = true) @WebParam(name = "subscriberName") String subscriberName);
	
	
}
