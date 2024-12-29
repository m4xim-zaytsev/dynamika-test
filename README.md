## инсутркция (1 вариант):
1. **в application.yaml указать свои настройки подключения к созданной бд (user,pass и порт)**
2. **запустить приложение/ mvn clean package , java -jar library.jar**

## инсутркция (2 вариант):
1. **cd /docker**
2. **в app.yaml сменить порт 5435 (или указать свои порты в настройках и композ файле)**"
2. **выполнить docker compose up**
2. **запустить приложение/ mvn clean package , java -jar library.jar**