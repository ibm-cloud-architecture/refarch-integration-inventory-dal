package inventory.ws;

import java.util.ArrayList;
import java.util.Collection;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class InventoryService {
	private String message = new String("Hello, ");
	
	@WebMethod
	public String sayHello(String name) {
	        return message + name + ".";
	}
	
	@WebMethod(operationName="items")
	public Collection<Item> getItems(){
		Item t = new Item("Smart phone");
		ArrayList<Item> l = new ArrayList<Item>();
		l.add(t);
		Item t2 = new Item("Tablet");
		l.add(t2);
		return l;
	}
}
