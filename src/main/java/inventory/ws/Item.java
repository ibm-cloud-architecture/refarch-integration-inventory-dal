package inventory.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import inventory.model.ItemEntity;

@XmlRootElement(name="Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	@XmlElement( nillable=true )
	protected long id;
	@XmlElement( nillable = false )
	protected String name;
	private String description;
    private double price;
	private String imgAlt;  
	private String img;
	private int quantity;
	private String type;
	private String serialNumber;
	private String model;

	
	public Item(){}
	
	public Item(String n){
		this.name=n;
	}
	
	public String toString(){
		return getId()+" "+getName();
	}
	
	 public Item(long id) { 
		    this.id = id;
	}

	public Item(String name, String description, int price, String img_alt, String img) {
	    this.name = name;
	    this.description = description;
	    this.price = price;
	    this.img = img;
	    this.imgAlt = img_alt; 
	  }

	public Item(ItemEntity ie,int q){
		this.name=ie.getName();
		this.description=ie.getDescription();
		this.id=ie.getId();
		this.img=ie.getImg();
		this.imgAlt=ie.getImg_alt();
		this.price=ie.getPrice();
		this.quantity=q;
		this.model=ie.getModel();
		this.type=ie.getProductType();
		this.serialNumber=ie.getSerialNumber();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImgAlt() {
		return imgAlt;
	}

	public void setImgAlt(String img_alt) {
		this.imgAlt = img_alt;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setPrice(double price) {
		this.price = price;
	}
	
}
