package payments;

import java.util.Collections;

import javax.persistence.AttributeConverter;

import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponse;

public class TransformConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String cc) {
		VaultOperations vaultOps = BeanUtil.getBean(VaultOperations.class);
		VaultResponse vaultResp = vaultOps.write("transform/encode/payments", Collections.singletonMap("value", cc));
		String encCC = vaultResp.getData().get("encoded_value").toString();
		return encCC;
	}

	@Override
	public String convertToEntityAttribute(String cc) {
		return cc;
	}

}
