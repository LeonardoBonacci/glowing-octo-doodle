package guru.bonacci.heroes.transferingress;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Profile("!kube")
@Configuration
public class KafkaTopics {

  @Bean
  public NewTopic request() {
    return TopicBuilder.name("request")
      .partitions(3)
      .build();
  }

  @Bean
  public NewTopic reply0() {
    return TopicBuilder.name("reply-0").build();
  }
  
  @Bean
  public NewTopic reply1() {
    return TopicBuilder.name("reply-1").build();
  }

  @Bean
  public NewTopic reply2() {
    return TopicBuilder.name("reply-2").build();
  }
}
