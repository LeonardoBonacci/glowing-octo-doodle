package guru.bonacci.heroes.transferingress;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import lombok.RequiredArgsConstructor;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private final KafkaUtils utils;

  @Value("${spring.kafka.bootstrap-servers}") String bootstrapServer;
  
  
  public ProducerFactory<String, String> validationProducerFactory() {
    return new DefaultKafkaProducerFactory<>(validationSenderProps());
  }

  private Map<String, Object> validationSenderProps() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    return props;
  }

  @Bean
  public ReplyingKafkaTemplate<String, String, String> replyingTemplate(
                    ProducerFactory<String, String> pf,
                    ConcurrentMessageListenerContainer<String, String> repliesContainer) {
    return new ReplyingKafkaTemplate<>(pf, repliesContainer);
  }

  @Bean
  public ConcurrentMessageListenerContainer<String, String> repliesContainer(
      ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

    ConcurrentMessageListenerContainer<String, String> repliesContainer = 
        containerFactory.createContainer("reply-" + utils.randomize());
    repliesContainer.getContainerProperties().setGroupId(UUID.randomUUID().toString()); // unique
    repliesContainer.setAutoStartup(false);

    Properties props = new Properties();
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    repliesContainer.getContainerProperties().setKafkaConsumerProperties(props);
    return repliesContainer;
  }
}
