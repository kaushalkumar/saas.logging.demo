
package saas.logging.demo.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import saas.logging.demo.object.RequestObject;

@XmlRootElement(name = "fetchGreeting", namespace = "http://demo.logging.saas/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fetchGreeting", namespace = "http://demo.logging.saas/")
public class FetchGreeting {

    @XmlElement(name = "requestObject", namespace = "", required = true)
    private RequestObject requestObject;

    /**
     * 
     * @return
     *     returns RequestObject
     */
    public RequestObject getRequestObject() {
        return this.requestObject;
    }

    /**
     * 
     * @param requestObject
     *     the value for the requestObject property
     */
    public void setRequestObject(RequestObject requestObject) {
        this.requestObject = requestObject;
    }

}
