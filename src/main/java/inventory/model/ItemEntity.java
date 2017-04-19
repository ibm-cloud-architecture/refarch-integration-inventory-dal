package inventory.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import inventory.ws.Item;

@Entity(name="Items")
public class ItemEntity {
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	protected long id;
	protected String name;
	private String description;
    private int price;
	private String img_alt;  
	private String img;
	private Date updateDate;
	private Date creationDate;
	
	public ItemEntity(){
		
	}
	
	public ItemEntity(Item i){
		this.name=i.getName();
		this.description=i.getDescription();
		this.id=i.getId();
		this.img=i.getImg();
		this.img_alt=i.getImg_alt();
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImg_alt() {
		return img_alt;
	}

	public void setImg_alt(String img_alt) {
		this.img_alt = img_alt;
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
	
}
