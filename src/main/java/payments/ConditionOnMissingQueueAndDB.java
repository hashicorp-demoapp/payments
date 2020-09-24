package payments;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

public class ConditionOnMissingQueueAndDB extends AllNestedConditions {

	ConditionOnMissingQueueAndDB() {

		super(ConfigurationPhase.REGISTER_BEAN);
	}

	@ConditionalOnMissingBean(DBPaymentsController.class)
	static class OnMissingQueue {
	}

	@ConditionalOnMissingBean(RedisPaymentController.class)
	static class OnMissingDB {
	}
}
