package inventory.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Inventory tracks the management of the item life cycle. It also maintains the relation with the supplier who
 * deliver the item to a site. The following are tracked in one inventory object
 * - the item delivered to a site with its quantity
 * - the transaction date
 * - who deliver the item
 * @author jeromeboyer
 *
 */
@Entity(name="Inventory")
@Table(name="INVENTORY")
@NamedQuery(name="Inventory.findAll", query="SELECT i FROM Inventory i")
public class Inventory {
	
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	private Long id=null;
	@Column(nullable=false)
	private Long itemId;
	@Column(precision=5)
	private Integer quantity;
	private Date updateDate;
	private Date creationDate;
	@Column(nullable=false, length=50)
	private String site;
	@Column(nullable=true)
	private Long supplierId;
	@Column(nullable=true)
	private Long customerId;
	private Double cost;
	private Double soldPrice;
	
	public Inventory(){}
	
	public Inventory(ItemEntity anItem){
		this.itemId=anItem.getId();
	}

	public String toString(){
		return getId()+" "+getItemId()+" "+getQuantity()+" @ "+getSite()+" "+getSupplierId();
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

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long item) {
		this.itemId = item;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(Double soldPrice) {
		this.soldPrice = soldPrice;
	}
	
}
