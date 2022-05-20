# glowing-octo-doodle

As explained here - https://strimzi.io/quickstarts/

```
cd requester
mvn clean install 
cd ..
cd replier
mvn clean install
cd replier
docker-compose build
docker-compose push
```

```
kubectl create namespace kafka
kubectl create -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
kubectl apply -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n kafka 
kubectl apply -f k8s/topics -n kafka
kubectl apply -f k8s/replier.yaml -n kafka
kubectl logs -f deploy/replier-app -n kafka
 
kubectl apply -f k8s/requester.yaml -n kafka

kubectl -n kafka run kafka-consumer -ti --image=quay.io/strimzi/kafka:0.28.0-kafka-3.1.0 --rm=true --restart=Never -- bin/kafka-console-consumer.sh --bootstrap-server my-cluster-kafka-bootstrap:9092 --topic reply-0 --from-beginning
```

Bored?

```
kubectl delete -f https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml -n kafka 
kubectl delete -f k8s -n kafka
```
