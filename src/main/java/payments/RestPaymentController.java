package payments;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Conditional(ConditionOnMissingQueueAndDB.class)
@RestController
public class RestPaymentController {

	Logger logger = LoggerFactory.getLogger(DBPaymentsController.class);

	@PostMapping("/")
	@ResponseBody
	public PaymentResponse pay(@RequestBody PaymentRequest request) {

		// Log it
		logger.info("New payment");
		logger.info("Fullname: {}", request.getName());
		logger.info("CC Type: {}", request.getType());
		logger.info("CC Number: {}", request.getNumber());
		logger.info("CC Expiration: {}", request.getExpiry());
		logger.info("CC CVC: {}", request.getCvc());

		return new PaymentResponse(UUID.randomUUID().toString(),
				"Payment processed successfully, card details returned for demo purposes, not for production",
				request.getNumber(), "Encryption Disabled");

	}

}
