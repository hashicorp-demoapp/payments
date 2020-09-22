package queue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Plaintext;

@WritingConverter
@Component
public class CreditCardToMapConverter implements Converter<CreditCard, Map<String, byte[]>> {

	@Autowired(required = false)
	VaultOperations vaultOperations;

	@Value("${app.encryption.enabled}")
	private Boolean encrypt;

	@Value("${app.encryption.key}")
	private String key;

	@Value("${app.encryption.path}")
	private String path;

	@Override
	public Map<String, byte[]> convert(CreditCard source) {

		Map<String, byte[]> ccMap = new HashMap<String, byte[]>();
		ccMap.put("type", source.getType().getBytes());
		ccMap.put("exp", source.getExpiry().getBytes());
		ccMap.put("cvc", source.getCvc().getBytes());

		if (encrypt == true) {
			Plaintext cc = Plaintext.of(source.getNumber());
			String cipherText = vaultOperations.opsForTransit(path).encrypt(key, cc).getCiphertext();
			ccMap.put("number", cipherText.getBytes());
		} else {
			ccMap.put("number", source.getNumber().getBytes());
		}

		return ccMap;
	}
}