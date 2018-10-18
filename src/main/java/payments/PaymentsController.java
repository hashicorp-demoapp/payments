package payments;

import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class PaymentsController {

    Logger logger = LoggerFactory.getLogger(PaymentsController.class);

    @GetMapping("/health")
    public String index() {
        return "Ok";
    }

    @PostMapping("/")
    @ResponseBody
    public PaymentResponse pay(@RequestBody PaymentRequest request) {
        logger.info("New payment");
        logger.info("Fullname: {}", request.getName());
        logger.info("CC Type: {}", request.getType());
        logger.info("CC Number: {}", request.getNumber());
        logger.info("CC Expiration: {}", request.getExpiry());
        logger.info("CC CVC: {}", request.getCvc());

        return new PaymentResponse("sorry insufficient funds");
    }

}