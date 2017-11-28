package inventory.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private Timestamp updateDate;
	private Timestamp creationDate;
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
