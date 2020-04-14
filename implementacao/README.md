## Teste ItHappens

## Pré requisito
- Maven 3
- Java 8
- Docker 1.13.0+

## Preparando ambiente

No diretório ithappens-backend executar o seguinte comando:
```
mvn clean package dockerfile:build 
```

## Executando com Docker Compose

No diretório implementacao executar
```
docker-compose up
```

## Acessando documentação

Após executar a aplicação, acessar: http://localhost:8080/swagger-ui.html
