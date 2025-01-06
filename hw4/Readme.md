1. Запустить docker-compose.yml
2. Запустить hw4/producer/src/main/java/org/example/Main.java - запустится producer
Начнут генериться события с рандомным ключом (от 0 до 9)
3. Запустить hw4/consumer/src/main/java/org/example/Main.java  - запустится consumer

События из топика 9 будут группироваться по ключу и будет считаться count. 

В лог приложения consumer будут выводиться результаты в разрезе временных окон. 



