package payments;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;

@Conditional(ConditionOnMissingQueueAndDB.class)
@RestController
public class RestPaymentController {

	Logger logger = LoggerFactory.getLogger(RestPaymentController.class);

	@Autowired
	Tracer tracer;

	@PostMapping("/")
	@ResponseBody
	public PaymentResponse pay(@RequestBody PaymentRequest request) {

		// Log it - tracer
		HashMap<String, String> cardSpanMap = new HashMap<>();
		cardSpanMap.put("name", request.getName());
		cardSpanMap.put("type", request.getType());
		cardSpanMap.put("number", request.getNumber());
		cardSpanMap.put("exp", request.getExpiry());

		Span cardSpan = tracer.buildSpan("process card").start();
		cardSpan.log(cardSpanMap);

		// Log it - stdout
		logger.info("New payment");
		logger.info("Fullname: {}", request.getName());
		logger.info("CC Type: {}", request.getType());
		logger.info("CC Number: {}", request.getNumber());
		logger.info("CC Expiration: {}", request.getExpiry());
		logger.info("CC CVC: {}", request.getCvc());

		cardSpan.finish();

		return new PaymentResponse(UUID.randomUUID().toString(),
				"Payment processed successfully, card details returned for demo purposes, not for production",
				request.getNumber(), "Encryption Disabled");

	}

}
