package payments;

import java.util.HashMap;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import database.DBPayment;
import database.DBPaymentRepository;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@Component
@EntityScan("database")
@EnableJpaRepositories("database")
@ComponentScan(basePackages = "database")
@ConditionalOnProperty(value = "app.storage", havingValue = "db", matchIfMissing = false)
public class DBPaymentController {

	Logger logger = LoggerFactory.getLogger(DBPaymentController.class);
	
	@Autowired
	Tracer tracer;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	DBPaymentRepository repo;

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

		// Log it - trace
		Span cardSpan = tracer.buildSpan("process card").start();
		
		// Put the payment in the DB
		DBPayment dbPayment = new DBPayment();
		dbPayment.setName(request.getName());
		dbPayment.setType(request.getType());
		dbPayment.setNumber(request.getNumber());
		dbPayment.setExpiry(request.getExpiry());
		dbPayment.setCvc(request.getCvc());
		repo.saveAndFlush(dbPayment);
		entityManager.clear();
		
		// close span
		String ccCipher = repo.findById(dbPayment.getId()).get().getNumber();
		HashMap<String, String> cardSpanMap = new HashMap<>();
		cardSpanMap.put("name", request.getName());
		cardSpanMap.put("type", request.getType());
		cardSpanMap.put("number", ccCipher);
		cardSpanMap.put("exp", request.getExpiry());
		cardSpan.log(cardSpanMap);
		cardSpan.finish();

		return new PaymentResponse(dbPayment.getId().toString(),
				"Payment processed successfully, card details returned for demo purposes, not for production",
				request.getNumber(), ccCipher);
	}

}