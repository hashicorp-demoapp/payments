package payments;

import java.util.HashMap;

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

import io.opentracing.Span;
import io.opentracing.Tracer;
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
	Tracer tracer;

	@Autowired
	QueuePaymentRepository repo;

	@PostMapping("/")
	@ResponseBody
	public PaymentResponse pay(@RequestBody PaymentRequest request) {

		// Log it - stdout
		logger.info("New payment");
		logger.info("Fullname: {}", request.getName());
		logger.info("CC Type: {}", request.getType());
		logger.info("CC Number: {}", request.getNumber());
		logger.info("CC Expiration: {}", request.getExpiry());
		logger.info("CC CVC: {}", request.getCvc());

		// Get the card
		CreditCard cc = new CreditCard();
		cc.setType(request.getType());
		cc.setNumber(request.getNumber());
		cc.setExpiry(request.getExpiry());
		cc.setCvc(request.getCvc());

		// Log it - trace
		Span cardSpan = tracer.buildSpan("process card").start();

		// Put the payment in queue
		RedisPayment payment = new RedisPayment();
		payment.setName(request.getName());
		payment.setCc(cc);
		repo.save(payment);

		// close span
		String ccCipher = repo.findById(payment.getId()).get().getCc().getNumber();
		HashMap<String, String> cardSpanMap = new HashMap<>();
		cardSpanMap.put("name", request.getName());
		cardSpanMap.put("type", request.getType());
		cardSpanMap.put("number", ccCipher);
		cardSpanMap.put("exp", request.getExpiry());
		cardSpan.log(cardSpanMap);
		cardSpan.finish();

		return new PaymentResponse(payment.getId().toString(),
				"Payment processed successfully, card details returned for demo purposes, not for production",
				request.getNumber(), ccCipher);
	}

}