1. Генерим хранилище ключей сервера и клиента:
   keytool -genkey -keyalg RSA -keystore server.keystore.jks -keypass 123456 -alias localhost -validity 365 -storetype pkcs12 -storepass 123456 -dname "CN=localhost,OU=Kafka,O=Otus,L=Moscow,ST=Moscow,C=RU"
   keytool -genkey -keyalg RSA -keystore client.keystore.jks -keypass 123456 -alias localhost -validity 365 -storetype pkcs12 -storepass 123456 -dname "CN=localhost,OU=Clients,O=Otus,L=Moscow,ST=Moscow,C=RU"
  
2. Генерим пару ключей CA
   openssl req -new -x509 -keyout ca-key -subj "/C=RU/ST=CA/L=CA/O=CA/OU=CA/CN=localhost" -out ca-cert -days 365

3. Импортируем в хранилища сервера и клиента CA серт
   keytool -keystore client.truststore.jks -alias CARoot --storepass 123456 -importcert -noprompt  -file ca-cert
   keytool -keystore server.truststore.jks -alias CARoot --storepass 123456 -importcert -noprompt  -file ca-cert

4. Подписываем серт
   keytool -keystore server.keystore.jks -alias localhost -certreq -storepass 123456 -keypass 123456 -file cert-file
   openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 365 -CAcreateserial -passin pass:123456
   keytool -keystore server.keystore.jks -alias CARoot -importcert --storepass 123456 -noprompt -file ca-cert
   keytool -keystore server.keystore.jks -alias localhost -importcert --storepass 123456 -noprompt -file cert-signed

   Результаты в корне hw2

5. Генерируем ID кластера
   export KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
Результат: echo $KAFKA_CLUSTER_ID
   N_SulNC6Rg22nmsbpOQEsw
6. Форматируем папки для журналов
   ./bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c /home/lexx/project/OTUS/HomeWork/hw2/server.properties
Результат:
   Formatting /tmp/kraft-combined-logs with metadata.version 3.8-IV0.

lexx@leva:/tmp/kraft-combined-logs$ ll
итого 32
drwxrwxr-x  2 lexx lexx  4096 дек  8 19:42 ./
drwxrwxrwt 22 root root 20480 дек  8 19:42 ../
-rw-rw-r--  1 lexx lexx   249 дек  8 19:42 bootstrap.checkpoint
-rw-rw-r--  1 lexx lexx   122 дек  8 19:42 meta.properties

// Не удалось настроить SSL - ошибка с сертификатом
Caused by: java.security.cert.CertificateException: No name matching leva found
Конфигурация описана в server_sasl_ssl.properties

Дальше выполнял без SSL. 

7. Запускаем брокер
   KAFKA_OPTS="-Djava.security.auth.login.config=/home/lexx/project/OTUS/HomeWork/hw2/kafka_server_jaas.conf"  ./bin/kafka-server-start.sh -daemon /home/lexx/project/OTUS/HomeWork/hw2/server_sasl.properties

Результат:
lexx@leva:~/project/OTUS/kafka_2.13-3.8.0$ jps
15412 Main
30200 Kafka
15866 RemoteMavenServer36
32335 Jps

8. Проверяем
./bin/kafka-cluster.sh cluster-id --config /home/lexx/project/OTUS/HomeWork/hw2/client-sasl.properties --bootstrap-server localhost:9094

Результат:
Cluster ID: N_SulNC6Rg22nmsbpOQEsw

9. Создаём топик
   ./bin/kafka-topics.sh --create --topic topic1 --bootstrap-server localhost:9094 --command-config /home/lexx/project/OTUS/HomeWork/hw2/client-sasl.properties
Результат:
   Created topic topic1.

10. Выдаём права пользователям на топик
     На запись пользователю Bob:
    ./bin/kafka-acls.sh --bootstrap-server localhost:9094 --add --allow-principal User:Bob --operation Write --topic topic1 --command-config /home/lexx/project/OTUS/HomeWork/hw2/client-admin.properties
    На чтение пользователю Alice
    ./bin/kafka-acls.sh --bootstrap-server localhost:9094 --add --allow-principal User:Alice --operation Read --topic topic1 --command-config /home/lexx/project/OTUS/HomeWork/hw2/client-admin.properties

Результат: screenshots/1.png

11. Проверяем от пользователя Bob
    ./bin/kafka-topics.sh --list --bootstrap-server localhost:9094 --command-config /home/lexx/project/OTUS/HomeWork/hw2/client-bob.properties
    ./bin/kafka-console-producer.sh --topic topic1 --bootstrap-server localhost:9094 --producer.config /home/lexx/project/OTUS/HomeWork/hw2/client-bob.properties
    ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9094 --topic topic1 --consumer.config /home/lexx/project/OTUS/HomeWork/hw2/client-bob.properties -from-beginning

Результат: screenshots/2.png, screenshots/3.png

12. Проверяем для пользователя Alice
    ./bin/kafka-topics.sh --list --bootstrap-server localhost:9094 --command-config /home/lexx/project/OTUS/HomeWork/hw2/client-alice.properties
    ./bin/kafka-console-producer.sh --topic topic1 --bootstrap-server localhost:9094 --producer.config /home/lexx/project/OTUS/HomeWork/hw2/client-alice.properties
    ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9094 --topic topic1 --consumer.config /home/lexx/project/OTUS/HomeWork/hw2/client-alice.properties -from-beginning

Результат: screenshots/4.png, screenshots/5.png

13. Проверка пользователя client
    ./bin/kafka-topics.sh --list --bootstrap-server localhost:9094 --command-config /home/lexx/project/OTUS/HomeWork/hw2/client-sasl.properties
    ./bin/kafka-console-producer.sh --topic topic1 --bootstrap-server localhost:9094 --producer.config /home/lexx/project/OTUS/HomeWork/hw2/client-sasl.properties
    ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9094 --topic topic1 --consumer.config /home/lexx/project/OTUS/HomeWork/hw2/client-sasl.properties -from-beginning

Результат: screenshots/6.png



