package inventory.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	protected long id;
	protected String name;
	private String description;
    private int price;
	private String img_alt;  
	private String img;

	
	public Item(){}
	
	public Item(String n){
		this.name=n;
	}
	
	 public Item(long id) { 
		    this.id = id;
	}

	public Item(String name, String description, int price, String img_alt, String img) {
	    this.name = name;
	    this.description = description;
	    this.price = price;
	    this.img = img;
	    this.img_alt = img_alt; 
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
	
}
