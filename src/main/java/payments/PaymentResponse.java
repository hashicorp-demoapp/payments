package payments;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;

class PaymentResponse {

	private String _id;
	private String _message;

	@JsonGetter("id")
	public String getId() {
		return _id;
	}

	@JsonGetter("message")
	public String getMessage() {
		return _message;
	}

	PaymentResponse(String message) {
		this._id = UUID.randomUUID().toString();
		this._message = message;
	}
}