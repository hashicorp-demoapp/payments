package queue;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;

@Configuration
public class RedisConfig {

	@Autowired
	CreditCardToMapConverter creditCardToMapConverter;

	@Bean
	RedisConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {

		RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		return template;
	}

	@Bean
	@ConditionalOnProperty(value = "app.encryption.enabled", havingValue = "true", matchIfMissing = false)
	public RedisCustomConversions redisCustomConversions() {
		return new RedisCustomConversions(Arrays.asList(creditCardToMapConverter));
	}

}
