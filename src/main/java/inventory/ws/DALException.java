package inventory.ws;

import javax.xml.ws.WebFault;

@WebFault(name="DALFault")
public class DALException extends Exception {

	private static final long serialVersionUID = 1L;
	
	DALFault faultBean;
	
	public DALException(String message, DALFault faultInfo) {
		super(message);
		this.faultBean=faultInfo;
	}
	
	public DALException(String message, DALFault faultInfo, Throwable cause) {
		super(message,cause);
		this.faultBean=faultInfo;
	}
	
	public DALException(String message){
		super(message);
		this.faultBean = new DALFault("ERR00",message);
	}
	
	public DALException(String code,String message){
		super(message);
		this.faultBean = new DALFault(code,message);
	}
	
	public DALException(String message, Throwable cause) {
		super(message,cause);
		this.faultBean = new DALFault("ERR00",message);
	}
	
	public DALException(Throwable cause) {
		super(cause);
		this.faultBean = new DALFault("ERR00","Exception not well coded");
	}
	
	
	public DALFault getFaultInfo() {
		return this.faultBean;
	}
}
