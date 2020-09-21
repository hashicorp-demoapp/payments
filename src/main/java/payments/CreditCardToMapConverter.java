package payments;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Plaintext;

@WritingConverter
public class CreditCardToMapConverter implements Converter<CreditCard, Map<String, byte[]>> {

	@Override
	public Map<String, byte[]> convert(CreditCard source) {
		// encrypt the CC

		VaultOperations vaultOps = BeanUtil.getBean(VaultOperations.class);
		Plaintext cc = Plaintext.of(source.getNumber());
		String cipherText = vaultOps.opsForTransit().encrypt("payment", cc).getCiphertext();

		Map<String, byte[]> ccMap = new HashMap<String, byte[]>();
		ccMap.put("type", source.getType().getBytes());
		ccMap.put("number", cipherText.getBytes());
		ccMap.put("exp", source.getExpiry().getBytes());
		ccMap.put("cvc", source.getCvc().getBytes());
		return ccMap;
	}
}