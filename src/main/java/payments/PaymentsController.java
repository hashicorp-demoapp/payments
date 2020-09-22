package payments;

import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

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
	private EntityManager entityManager;

	@Autowired
	QueuePaymentRepository queueRepo;

	@Autowired
	DBPaymentRepository dbRepo;

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

		RedisPayment payment = new RedisPayment();
		payment.setName(request.getName());
		payment.setCc(cc);

		// Queue
		queueRepo.save(payment);
		// String encCC = queueRepo.findById(payment.getId()).get().getCc().getNumber();

		// DB
		DBPayment dbPayment = new DBPayment();
		dbPayment.setName(request.getName());
		dbPayment.setType(request.getType());
		dbPayment.setNumber(request.getNumber());
		dbPayment.setExpiry(request.getExpiry());
		dbPayment.setCvc(request.getCvc());
		dbRepo.saveAndFlush(dbPayment);

		entityManager.clear();
		String encCC = dbRepo.findById(dbPayment.getId()).get().getNumber();

		return new PaymentResponse(dbPayment.getId().toString(), "Payment processed successfully", request.getNumber(),
				encCC);
	}

}