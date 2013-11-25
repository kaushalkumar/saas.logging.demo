package saas.logging.demo;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import saas.logging.demo.object.RequestObject;
import saas.logging.demo.object.ResponseObject;


@WebService(targetNamespace = "http://demo.logging.saas/")
public interface SaaSLoggingService {

	@WebResult(name = "responseObject")
	ResponseObject fetchGreeting(@XmlElement(required = true) @WebParam(name = "requestObject") RequestObject requestObject);
	
}
