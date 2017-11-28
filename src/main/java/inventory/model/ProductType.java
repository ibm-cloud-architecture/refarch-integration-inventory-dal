package inventory.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProductType {
	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE)
	protected Long id=null;
	@Column(nullable=true, length=2000)
	private String description;
	@Column(nullable=false, length=100)
	protected String name;
	@Column(precision=8, scale=2)
	private Double unitPrice=null;
}
