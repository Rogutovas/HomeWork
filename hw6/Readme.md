1. Запустить docker-compose.yml
2. Создаём таблицу customers
   CREATE TABLE customers (id INT PRIMARY KEY, name TEXT, age INT);
3. Создаём коннектор
curl -X POST --data-binary "@customers.json" -H "Content-Type: application/json" http://localhost:8083/connectors | jq
4. Добавляем записи в таблицу
   INSERT INTO customers (id, name, age) VALUES (5, 'Fred', 34);
   INSERT INTO customers (id, name, age) VALUES (7, 'Sue', 25);
   INSERT INTO customers (id, name, age) VALUES (2, 'Bill', 51);
5. Читаем топик postgres.public.customers
   docker exec kafka1 kafka-console-consumer --topic postgres.public.customers --bootstrap-server kafka1:19092,kafka2:19093,kafka3:19094 --property print.offset=true --property print.key=true --from-beginning
6. Видим записи в топике. СМ скрины 1.png, 2.png



