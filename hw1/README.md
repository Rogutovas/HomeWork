1. Скачиваем, распаковываем kafka с сайта https://kafka.apache.org/downloads   
2. Запускаем сервисы zookeeper и kafka с конфигами по умолчанию:
   ./bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
   ./bin/kafka-server-start.sh -daemon config/server.properties
Результат: Запуск кафка.png
3. Создаём топик test 
   ./bin/kafka-topics.sh --create --topic test --bootstrap-server localhost:9092
   ./bin/kafka-topics.sh --describe --topic test --bootstrap-server localhost:9092
4. Отправим несколько сообщений:
   ./bin/kafka-console-producer.sh --topic test --bootstrap-server localhost:9092
Результат: Добавление записей.png
5. Читаем сообщения:
   ./bin/kafka-console-consumer.sh --topic test --bootstrap-server localhost:9092 --from-beginning
Результат: Чтение.png


P.S. Спасибо за внимание



