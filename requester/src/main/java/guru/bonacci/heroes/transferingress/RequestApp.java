package guru.bonacci.heroes.transferingress;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class RequestApp {

  @Value("${num.partitions}") int numPartitions;

  
	public static void main(String[] args) {
		SpringApplication.run(RequestApp.class, args);
	}

	@Bean
  CommandLineRunner demo(ReplyingKafkaTemplate<String, String, String> kafkaTemplate, KafkaUtils utils) {
    return args -> {
      while(true) {
        var random = UUID.randomUUID().toString();
        
        var record = new ProducerRecord<>("request", random, random);
        record.headers().add(new RecordHeader("topic-suffix", utils.randomize().getBytes()));
  
        var replyFuture = kafkaTemplate.sendAndReceive(record);
  
        replyFuture.addCallback(
            result -> {
                log.info("finally - a reply: {}", result);
            },
            ex -> {
                log.info("oops - a complaint: {}", ex.getMessage());
            }
        );
        
        Thread.sleep(1000);
      }  
   };
  }
}
