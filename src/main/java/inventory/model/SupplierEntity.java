package inventory.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import inventory.ws.Supplier;

@Entity(name="Supplier")
@Table(name="SUPPLIERS")
@NamedQuery(name="Supplier.findAll", query="SELECT i FROM Supplier i")
public class SupplierEntity extends Party {

	@Column(nullable=true, length=200)
	protected String street;
	@Column(nullable=true, length=50)
	protected String city;
	@Column(nullable=true, length=10)
	protected String zipcode;
	@Column(nullable=true, length=50)
	protected String state;

	
	public SupplierEntity(){}
	
	public SupplierEntity(Supplier s){
		this.name=s.getName();
		this.id=s.getId();
		this.type=s.getType();
		this.status=s.getStatus();
		this.street=s.getStreet();
		this.city=s.getCity();
		this.zipcode=s.getZipcode();
		this.state=s.getState();
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

	
}
