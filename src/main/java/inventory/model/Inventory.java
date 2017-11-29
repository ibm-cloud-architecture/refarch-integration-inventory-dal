package inventory.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name="Inventory")
@Table(name="INVENTORY")
@NamedQuery(name="Inventory.findAll", query="SELECT i FROM Inventory i")
public class Inventory {
	
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private Long id=null;
//	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	//@JoinColumn(name="ITEM_ID")
	private Long item;
	@Column(precision=5)
	private Integer quantity;
	private Date updateDate;
	private Date creationDate;
	@Column(nullable=true, length=50)
	private String site;
	
	public Inventory(){}
	
	public Inventory(ItemEntity anItem){
		this.item=anItem.getId();
	}

	public String toString(){
		return getId()+" "+getItem()+" "+getQuantity()+" @ "+getSite();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Long getItem() {
		return item;
	}

	public void setItem(Long item) {
		this.item = item;
	}
	
}
