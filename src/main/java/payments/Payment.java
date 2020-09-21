package payments;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("payment")
@TypeAlias("payments")
class Payment {

	@Id
	private String Id;
	private String name;
	private CreditCard cc;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CreditCard getCc() {
		return cc;
	}

	public void setCc(CreditCard cc) {
		this.cc = cc;
	}

	@Override
	public String toString() {
		return "Payment [Id=" + Id + ", name=" + name + ", cc=" + cc + "]";
	}

}
