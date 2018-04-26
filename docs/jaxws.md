# JAXWS Summary
Java API for XML Web Services, or JAX-WS 2.0, is part of the Java EE 5 platform. Using a powerful combination of Java 5 annotations and Ant-compatible tools to mask the underlying complexity of the SOAP protocol, JAX-WS 2.0 greatly simplifies the development of web services and of web-service-based SOA architectures with Java.

# Main Description
Java API for XML-Based Web Services is the new generation of JAX-RPC with features such as:

* support for JAXB 2.0-based data binding
* support for the latest W3C and WS-I standards (e.g. SOAP 1.2, WSDL 1.2)
* standardized meta-data for Java<->WSDL mapping
* ease-of-development features
* support for easier evolution of Web services
* an improved handler framework
* support for asynchronous RPC and non-HTTP transports

In JAX-WS 2.0, a web service implementation simply takes the form of an ordinary Java class with some special annotations.

JAX-WS 2.0 provides a library of annotations and a toolkit for Ant tasks and command-line utilities that hide the complexity of the underlying XML message protocol. JAX-WS does leverage JDK 5 annotations in order to express web service meta data on Java components and to describe the mapping between Java data types and XML.

One important decision when developing WS is to decide whether to start from the WSDL or from the java component. If we are responsible to implement the service we will start from the java code and then expose the service using generated WSDL. When starting with a java class, the developer needs to follow the following path:

* Define using java annotations the web service to expose
* (optional)Generate Web service elements like wsdl, and proxy classes
* Package the files into a war.
* Deploy the war to a servlet container. It is possible to use a JSE container and not a http server. JAX-WS runtime will generate WSDL, will translate SOAP request to java and will return into SOAP response
* Develop a client project to call the service, use Ant or Maven wsimport to import the WSDL and generate


# Starting from JAVA
We must provide the JAX-WS tools with a valid endpoint implementation class. This implementation class is the class that implements the desired web service. A valid endpoint implementation class must meet the following requirements:

* It must carry a javax.jws.WebService annotation
* Any of its methods may carry a javax.jws.WebMethod annotation
* All of its methods may throw java.rmi.RemoteException in addition to any service-specific exceptions.
* All method parameters and return types must be compatible with the JAXB 2.0 Java to XML Schema mapping definition.
* A method parameter or return value type must not implement the java.rmi.Remote interface either directly or indirectly.

When starting from a Java endpoint implementation class, it is recommended that the portable artifacts be generated from source using apt. This because the JAX-WS tools will then have full access to the source code and will be able to utilize parameter names that are otherwise not available through the Java reflection APIs.

To expose a java class as a web service we need to add the annotations @WebService and @WebMethod to the implementation class. @WebService declares the class as a web service. The name property lets define the web service name (the wdsl:portType attribute in WDSL 1.1). The serviceName property lets you define the WDSL service name. The @WebMethod annotation is used to indicate which methods are to be exposed by this web service.
```java
@WebService
public class DALService {

   @WebMethod(operationName="newItem")
	public Item newItem(Item inItem) throws DALException {
		...
	}
}
```

To get no error during edition and compilation we need to include the following jar files in the project classpath:  
`jaxws-api.jar` and the Java `tools.jar`

The above definitions will generate a wsdl with the following elements:
```xml
<wsdl:portType name="DALService">
	<wsdl:operation name="newItem">
		<wsdl:input message="tns:newItem" name="newItem"></wsdl:input>
		<wsdl:output message="tns:newItemResponse" name="newItemResponse"></wsdl:output>
		<wsdl:fault message="tns:DALException" name="DALException"></wsdl:fault>
	</wsdl:operation>
</wsdl:portType>
<wsdl:binding name="DALServiceServiceSoapBinding" type="tns:DALService">
	<soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
			<wsdl:operation name="newItem">
				<soap:operation soapAction="" style="document"/>
				<wsdl:input name="newItem">
					<soap:body use="literal"/>
				</wsdl:input>
				<wsdl:output name="newItemResponse">
					<soap:body use="literal"/>
				</wsdl:output>
				<wsdl:fault name="DALException">
					<soap:fault name="DALException" use="literal"/>
				</wsdl:fault>
			</wsdl:operation>
```
The SOAP binding is using HTTP transport and is document style.

The project needs to include a web.xml file to expose the service and to map it to a URL pattern (`/ws`):

```xml
	<servlet>
		<display-name>Data Access Layer Service for Brown</display-name>
		<servlet-name>DALService</servlet-name>
		<servlet-class>inventory.ws.DALService</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>DALService</servlet-name>
		<url-pattern>/ws</url-pattern>
	</servlet-mapping>
```

When deployed to an application server like WebSphere Liberty the wsdl is automatically created and visible under the URL path:
`http://hostname:9080/appcontext/ws?wsdl`

The DALException is to report issue from the server to the client and will be mapped to a SOAP Fault:
```xml
<xs:complexType name="dalFault">
	<xs:sequence>
		<xs:element minOccurs="0" name="code" type="xs:string"/>
		<xs:element minOccurs="0" name="message" type="xs:string"/>
	</xs:sequence>
</xs:complexType>
```

## Client side
JAX-WS relies on JAXB for data binding. When you invoke *wsimport* Tool on a wsdl, it in-turn calls XJC tool in JAXB RI to generate the beans used by the JAX-WS runtime. For each element types of the XML schema there is a java class. The name of the class is the name of the xml type, and can be enforced by the annotation @XmlType. The package name is coming from the namespace defined in the wsimport command.
