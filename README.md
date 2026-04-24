# Gestion de Bibliotheque — Backend

Application Spring Boot pour la gestion de bibliotheque.

**Stack:** Java 17 · Spring Boot 2.7.18 · H2 · JPA · MapStruct · Lombok

## Prerequis

- Java 17
- Maven 3.8+

## Lancer le backend

Ouvrez un terminal a la racine du projet puis executez:

```bash
mvn clean install
mvn spring-boot:run
```

Le backend demarre sur:
- API: `http://localhost:8080/api`
- Console H2: `http://localhost:8080/h2-console`

Parametres H2:
- JDBC URL: `jdbc:h2:mem:biblioDb`
- User: `sa`
- Password: (vide)

## Donnees chargees au demarrage

Le backend injecte automatiquement des donnees de demo au startup:
- Auteurs: Victor Hugo, Albert Camus
- Categories: Roman, Philosophie
- Editeurs: Gallimard, Flammarion
- Livres: Les Miserables, Notre-Dame de Paris, L'Etranger, Le Mythe de Sisyphe

## Executer les tests

```bash
mvn test
```

## Tester l'API avec Postman

1. Ouvrir Postman
2. Importer `Bibliotheque_API.postman_collection.json` (racine du projet)
3. Verifier la variable de collection:
   - `baseUrl = http://localhost:8080`
4. Executer les requetes dans les dossiers:
   - Auteurs
   - Livres
   - Categories
   - Editeurs

---

Merci a notre professeur pour son encadrement et ses precieux conseils.