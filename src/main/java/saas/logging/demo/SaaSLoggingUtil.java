package saas.logging.demo;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import saas.logging.demo.object.Logs;

/**
 * SaaSLoggingUtil - Util class for static method of XML conversions
 *
 */
public final class SaaSLoggingUtil {

	private static Map<String, JAXBContext> jaxbContexts = new HashMap<String, JAXBContext>();
	
	/**
	 * getJaxbContext - returns the cached JAXBContext for a class.
	 * 
	 * @param jaxbContextClass Class
	 * @return JAXBContext
	 */
	public static JAXBContext getJaxbContext(Class<?> jaxbContextClass) {
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
	
	/**
	 * convertLogsToDocument - convertLogsToDocument.
	 * @param logs Logs
	 * @return Document
	 */
	public static Document convertLogsToDocument(Logs logs) {
		Document logsDocument;
		try {
			JAXBContext jaxbContext = getJaxbContext(Logs.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			JAXBElement<Logs> response2JaxbElement = new JAXBElement<Logs>(new QName(null, "Logs"),	Logs.class, logs);
			logsDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			marshaller.marshal(response2JaxbElement, logsDocument);
			return logsDocument;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
