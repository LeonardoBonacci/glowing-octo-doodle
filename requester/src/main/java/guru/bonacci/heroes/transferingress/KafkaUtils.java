package guru.bonacci.heroes.transferingress;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaUtils {

  @Value("${num.partitions}") int numPartitions;
  @Value("${pod.name:foo}") String podName;

  
  public String randomize() {
    log.info("name {}", podName);
    int number = Math.abs(podName.hashCode()) % numPartitions;
    log.info("pseudo-random {}", number);
    return String.valueOf(number);
  }
}
