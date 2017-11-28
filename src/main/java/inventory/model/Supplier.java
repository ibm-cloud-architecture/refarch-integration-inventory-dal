package inventory.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name="Supplier")
@Table(name="SUPPLIERS")
@NamedQuery(name="Supplier.findAll", query="SELECT i FROM Supplier i")
public class Supplier extends Party {

	@Column(nullable=true, length=200)
	protected String street;
	@Column(nullable=true, length=50)
	protected String city;
	@Column(nullable=true, length=10)
	protected String zipcode;
	@Column(nullable=true, length=50)
	protected String state;
	@JoinTable(name="supplier_delivers_item")
	@ManyToMany(cascade=CascadeType.MERGE)
	protected Collection<ItemEntity> items;
	
	public Supplier(){}
	
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


	public void addItemEntity(ItemEntity ie) {
		getItems().add(ie);
	}

	public Collection<ItemEntity> getItems() {
		if (items == null) items= new ArrayList<ItemEntity>();
		return items;
	}

	public void setItems(Collection<ItemEntity> items) {
		this.items = items;
	}

	
}
