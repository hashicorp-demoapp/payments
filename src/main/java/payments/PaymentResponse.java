package payments;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;

class PaymentResponse {

	private String _id;
	private String _message;
	private String _plaintext;
	private String _ciphertext;

	@JsonGetter("id")
	public String getId() {
		return _id;
	}

	@JsonGetter("message")
	public String getMessage() {
		return _message;
	}

	@JsonGetter("card_plaintext")
	public String getPlaintext() {
		return _plaintext;
	}

	@JsonGetter("card_ciphertext")
	public String getCiphertext() {
		return _ciphertext;
	}

	PaymentResponse(String id, String message, String plaintext, String ciphertext) {
		this._id = id;
		this._message = message;
		this._plaintext = plaintext;
		this._ciphertext = ciphertext;
	}
}