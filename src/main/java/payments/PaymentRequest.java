package payments;

class PaymentRequest {

  private String name;
  private String type;
  private String number;
  private String expiry;
  private String cvc;

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getNumber() {
    return number;
  }

  public String getExpiry() {
    return expiry;
  }

  public String getCvc() {
    return cvc;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setExpiry(String exp) {
    this.expiry = exp;
  }

  public void setCvc(String cvc) {
    this.cvc = cvc;
  }
}
