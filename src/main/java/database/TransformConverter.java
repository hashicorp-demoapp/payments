package database;

import java.util.Collections;

import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponse;

@Component
public class TransformConverter implements AttributeConverter<String, String> {

	@Value("${app.encryption.enabled:false}")
	private Boolean encrypt;

	@Value("${app.encryption.path:transform}")
	private String path;
	
	@Value("${app.encryption.key:payments}")
	private String key;

	@Autowired(required = false)
	VaultOperations vaultOperations;

	@Override
	public String convertToDatabaseColumn(String cc) {

		String cardCipher;

		if (encrypt == true) {
			VaultResponse vaultResp = vaultOperations.write(path + "/encode/" + key,
					Collections.singletonMap("value", cc));
			cardCipher = vaultResp.getData().get("encoded_value").toString();
		} else {
			cardCipher = cc;
		}

		return cardCipher;
	}

	@Override
	public String convertToEntityAttribute(String cc) {
		return cc;
	}

}
