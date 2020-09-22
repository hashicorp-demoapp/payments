package payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import queue.CreditCard;
import queue.QueuePaymentRepository;
import queue.RedisPayment;

@RestController
@Component
@EntityScan("queue")
@EnableRedisRepositories("queue")
@ComponentScan(basePackages = "queue")
@ConditionalOnProperty(value = "app.storage", havingValue = "redis", matchIfMissing = false)
public class RedisPaymentController {

	Logger logger = LoggerFactory.getLogger(RedisPaymentController.class);

	@Autowired
	QueuePaymentRepository repo;

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

		repo.save(payment);
		String ccCipher = repo.findById(payment.getId()).get().getCc().getNumber();

		return new PaymentResponse(payment.getId().toString(), "Payment processed successfully", request.getNumber(),
				ccCipher);
	}

}