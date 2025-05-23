# rss25SB

## Description  
Application RESTful pour la gestion et la diffusion de flux RSS personnalisés selon la spécification rss25SB.  
Projet développé en Spring Boot avec PostgreSQL comme base de données.

---

## Prérequis  
- Java 17 ou supérieur  
- Maven 3.6+  
- PostgreSQL  
- Postman 

---

## Installation et exécution locale

1. **Cloner le dépôt**  
```bash
git clone https://github.com/arijsg/Projet_XML.git
cd Projet_XML
```

2. Configurer la base PostgreSQL

- Créez une base `rss25` (ou autre selon votre choix)
- Créez un utilisateur `rssuser` avec mot de passe `rsspass` et donnez-lui les droits nécessaires

3. Modifier le fichier `src/main/resources/application.properties` pour correspondre à votre environnement local :
spring.datasource.url=jdbc:postgresql://localhost:5432/rss25
spring.datasource.username=rssuser
spring.datasource.password=rsspass
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

spring.h2.console.enabled=false

server.port=8080

logging.level.org.springframework=DEBUG
logging.level.fr.univrouen.rss25SB=DEBUG
logging.level.org.hibernate=ERROR

## Lancement local

Compilez et lancez le projet :
```
mvn clean install
mvn spring-boot:run
```
L’application sera accessible sur :  
http://localhost:8080

4. Lancer le projet déployer sur clever cloud via l'url:
https://app-102c9823-5b76-4799-b83a-9ebfa90f56d0.cleverapps.io/rss25SB/resume/html


