package inventory.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@Column(nullable=false)
	protected long id;
	 
	@Column(nullable=false, length=1500)
	private String description;
	@Column(nullable=false, length=100)
	protected String name;
	@Column(nullable=false, precision=8, scale=2)
	private double price;
	@Column(nullable=false, length=100)
	private String img;
	@Column(name="IMG_ALT", length=75)
	private String imgAlt;
	@Column(precision=5)
	private int quantity;
	private Timestamp updateDate;
	private Timestamp creationDate;
	
	public ItemEntity(){
		
	}
	
	public ItemEntity(Item i){
		this.name=i.getName();
		this.description=i.getDescription();
		this.id=i.getId();
		this.img=i.getImg();
		this.imgAlt=i.getImgAlt();
		this.price=i.getPrice();
	}
	
	public ItemEntity(String name){
		this.name=name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	
}
