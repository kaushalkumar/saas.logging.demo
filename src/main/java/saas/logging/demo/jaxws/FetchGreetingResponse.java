
package saas.logging.demo.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import saas.logging.demo.object.ResponseObject;

@XmlRootElement(name = "fetchGreetingResponse", namespace = "http://demo.logging.saas/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchGreetingResponse", namespace = "http://demo.logging.saas/")
public class FetchGreetingResponse {

    @XmlElement(name = "responseObject", namespace = "")
    private ResponseObject responseObject;

    /**
     * 
     * @return
     *     returns ResponseObject
     */
    public ResponseObject getResponseObject() {
        return this.responseObject;
    }

    /**
     * 
     * @param responseObject
     *     the value for the responseObject property
     */
    public void setResponseObject(ResponseObject responseObject) {
        this.responseObject = responseObject;
    }

}
