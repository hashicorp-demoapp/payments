package payments;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class CreditCardToMapConverter implements Converter<CreditCard, Map<String, byte[]>> {

	@Override
	public Map<String, byte[]> convert(CreditCard source) {
		Map<String, byte[]> cc = new HashMap<String, byte[]>();
		cc.put("type", source.getType().getBytes());
		cc.put("number", "masked".getBytes());
		cc.put("exp", source.getExpiry().getBytes());
		cc.put("cvc", source.getCvc().getBytes());
		return cc;
	}
}