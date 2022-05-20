package guru.bonacci.kafka.replier;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.RecordContext;
import org.apache.kafka.streams.processor.TopicNameExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableKafkaStreams
@SpringBootApplication
@RequiredArgsConstructor
public class ReplyApp {

  
  public static void main(String[] args) {
    SpringApplication.run(ReplyApp.class, args);
	}

	@Bean
  public KStream<String, String> topology(StreamsBuilder builder) {
    KStream<String, String> requestStream = builder.stream("request");
    requestStream.to(new DynamicTopicNameExtractor<String, String>());
    return requestStream;
  }

	@ToString
	@EqualsAndHashCode
	public static class DynamicTopicNameExtractor<K, V> implements TopicNameExtractor<K, V> {

	  private static final String TOPIC_NAME = "reply-";
	  
    public String extract(final K key, final V value, final RecordContext recordContext) {
      var suffix = new String(recordContext.headers().lastHeader("topic-suffix").value());
      log.info("Reply me through {}", TOPIC_NAME + suffix);
      return TOPIC_NAME + suffix;
    }
	}
}
