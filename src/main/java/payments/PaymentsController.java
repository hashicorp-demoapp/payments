package payments;

import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@Component
public class PaymentsController {

	Logger logger = LoggerFactory.getLogger(PaymentsController.class);

	@Autowired
	PaymentRepository repo;

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

		CreditCard cc = new CreditCard();
		cc.setType(request.getType());
		cc.setNumber(request.getNumber());
		cc.setExpiry(request.getExpiry());
		cc.setCvc(request.getCvc());

		Payment payment = new Payment();
		payment.setName(request.getName());
		payment.setCc(cc);

		// Redis
		repo.save(payment);
		String encCC = repo.findById(payment.getId()).get().getCc().getNumber();

		return new PaymentResponse(payment.getId(), "Payment processed successfully", request.getNumber(), encCC);
	}

}