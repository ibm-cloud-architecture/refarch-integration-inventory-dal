package inventory.ws.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import inventory.model.SupplierEntity;

@XmlRootElement(name="Supplier")
@XmlAccessorType(XmlAccessType.FIELD)
public class Supplier {
	@XmlElement( nillable=true )
	protected Long id=null;
	protected String name;
	protected String type;
	protected String status;
	protected String street;
	protected String city;
	protected String zipcode;
	protected String state;

	
	public Supplier(){}
	
	public Supplier(SupplierEntity se){
		this.id=se.getId();
		this.name=se.getName();
		this.type=se.getType();
		this.status=se.getStatus();
		this.street=se.getStreet();
		this.city=se.getCity();
		this.zipcode=se.getZipcode();
		this.state=se.getState();
	}
	
	public String toString(){
		return getId()+" "+getName()+" "+getStatus();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long sid) {
		this.id = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
