package queue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueuePaymentRepository extends CrudRepository<RedisPayment, String> {
}