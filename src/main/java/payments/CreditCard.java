package payments;

public class CreditCard {

	public CreditCard(String type, String number, String expiry, String cvc) {
		super();
		this.type = type;
		this.number = number;
		this.expiry = expiry;
		this.cvc = cvc;
	}

	public CreditCard() {
	}

	private String type;
	private String number;
	private String expiry;
	private String cvc;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

}
