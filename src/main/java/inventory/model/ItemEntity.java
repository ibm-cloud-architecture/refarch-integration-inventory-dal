package inventory.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import inventory.ws.Item;

@Entity(name="Item")
@Table(name="ITEMS")
@NamedQuery(name="Item.findAll", query="SELECT i FROM Item i")
public class ItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	protected Long id=null;
	@Column(nullable=false, length=100)
	protected String name;
	@Column(nullable=true, length=50)
	protected String serialNumber;
	@Column(nullable=true, length=50)
	private String productType;  // type is a predefined keyword in db2 better to have another name
	@Column(nullable=true, length=50)
	private String model;
	@Column(nullable=true, length=5000)
	private String description;
	@Column(precision=8, scale=2)
	private Double price=null;
	@Column(nullable=true, length=50)
	private String img;
	@Column(name="IMG_ALT", length=75)
	private String imgAlt;
	
	@ManyToMany(mappedBy = "items")
	protected Collection<SupplierEntity> suppliers;
	private Date updateDate;
	private Date creationDate;

	public ItemEntity(){

	}

	public ItemEntity(Item i){
		this.name=i.getName();
		this.description=i.getDescription();
		if (i.getId() <=0) setId(null);
		else setId(i.getId());
		this.img=i.getImg();
		this.imgAlt=i.getImgAlt();
		this.price=i.getPrice();
	}

	public ItemEntity(String name){
		this.name=name;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = new Long(id);
	}
	
	public void setId(Long i){
		this.id=i;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = new Double(price);
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}


	public String getImg_alt() {
		return imgAlt;
	}

	public void setImg_alt(String img_alt) {
		this.imgAlt = img_alt;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getImgAlt() {
		return imgAlt;
	}

	public void setImgAlt(String imgAlt) {
		this.imgAlt = imgAlt;
	}

	public Collection<SupplierEntity> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Collection<SupplierEntity> suppliers) {
		this.suppliers = suppliers;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
}
