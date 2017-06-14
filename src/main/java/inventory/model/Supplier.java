package inventory.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name="Supplier")
@Table(name="SUPPLIERS")
@NamedQuery(name="Supplier.findAll", query="SELECT i FROM Supplier i")
public class Supplier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8580561240813581595L;

	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	protected Long id=null;
	protected String name;
	protected String street;
	protected String city;
	protected String zipcode;
	protected String state;
	protected String status;
	protected Timestamp creationDate;
	protected Timestamp updateDate;
	
	public Supplier(){}

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

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
