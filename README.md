# GitHub & GitLab Service

## Instructions
Implement service in Java and Spring (or equivalent FW) that gathers user and his/her repositories from both GitHub and GitLab.\
Use git and keep clean history with commits like in regular project.\
Use comments in your code.\
Service will have just one endpoint:
```
GET /users/{user}
```
Example result:
```json
{
  "github": {
    "id": 1234,
    "username": "steven1234",
    "repositories": [
      {
        "id": 1234,
        "name": "Example GitHub Service"
      }
    ]
  },
  "gitlab": {
    "id": 4321,
    "username": "steven1234",
    "repositories": [
      {
        "id": 4321,
        "name": "Example GitLab Service"
      }
    ]
  }
}
```

For retrieving GitLab user, please use provided **GitLab Service**.\
**This service has some issues!**
- Some of them are functional (application is not working as expected) and some of them are architectural issues.
- Review the code and fix functional issues.
- You don't have to fix architectural issues, just write them down.

This project is using google java formatting.\
For retrieving GitHub user, use GitHub API described [here](https://docs.github.com/en/rest)

## Solution
The solution is implemented in Java using Spring Boot framework. 
The service exposes a single endpoint to fetch user details and their repositories from both GitHub and GitLab.
The code is structured into controllers, services, and models for better organization and maintainability.
The database is created and initialized using Liquibase, while MapStruct handles mapping between DTOs and entities. 
The service’s DTOs and API are generated using an API-first approach with OpenAPI.

The main service, `github-gitlab-service`, calls the `gitlab-service` to fetch information 
from _GitLab_ and uses the `GitHub API` to retrieve information from _GitHub_.

`github-gitlab-service` uses database cache to store user and repository information 
to minimize API calls to GitHub and GitLab. The service is prepared to support more git clients in the future.

### Running the Application
To run the whole application with both services and databases, use Docker Compose:

```bash
docker compose -f docker-compose-all.yml up -d
```

This command will build and start the services along with their databases.

You can also run `gitlab-service` only with both databases:

```bash
docker compose -f docker-compose-local.yml up -d
```
This command will build and start the `gitlab-service` only and both databases. 
_It is useful for development and testing purposes of github-gitlab-service._


### Accessing the Service
Once the services are running, you can access the `github-gitlab-service` at:
```
http://localhost:8087/api/users/{username}
```
Replace `{username}` with the desired GitHub/GitLab username. Default port is `8087`. HTTP method is `GET`.

### Example Request
```bash
curl -X GET "http://localhost:8087/api/users/steven1234" -H "accept: application/json"
```
### Example Response
```json
{
   "data":[
      {
         "source":"GITHUB",
         "id":3795741,
         "username":"steven1234",
         "lastUpdate":"2025-09-15T09:19:34.892220675",
         "repositories":[
            {
               "id":95628557,
               "name":"antd-mobile-create-react-app"
            },
            {
               "id":92251637,
               "name":"mongo"
            }
         ]
      },
      {
         "source":"GITLAB",
         "id":6293485,
         "username":"Steven1234",
         "lastUpdate":"15.09.2025T09:19:35",
         "repositories":[]
      }
   ]
}
```

## Issues in gitlab-service
- Inappropriate package structure design - e.g.:
  - class `eu.profinit.gitlabservice.controller.dto.UserWithProjects` contains database entities (`GitLabUser`, `GitLabProject`) instead of only DTOs, and this class is used in `GitLabService` in `eu.profinit.gitlabservice.service` package.
  - package `eu.profinit.gitlabservice.database.model` contains entities which are used almost everywhere in the project, not only in the database layer.
  - package `eu.profinit.gitlabservice.service.dto` contains DTOs, which are used in `DatabaseService`.
- Dependency inversion principle is not followed in services—lack of interfaces.
- Using database entities in the response – in the class `eu.profinit.gitlabservice.controller.dto.UserWithProjects`, the entities `GitLabUser` and `GitLabProject` are being used.
- Field injection instead of constructor injection: `eu.profinit.gitlabservice.config.GitLabServiceConfiguration`
- In the class `eu.profinit.gitlabservice.database.model.GitLabProject` there is `@ManyToOne` with `FetchType.EAGER` (by default). It is better to use `FetchType.LAZY` to avoid unnecessary data loading.
- Lack of exception handling – for example, if the `GitLab API` is down or user in GitLab does not exist, the service will return a 500 error without any meaningful message.
- Lack of null checks in `eu.profinit.gitlabservice.service.GitLabService`
- Potential issues with `@Transactional` in `eu.profinit.gitlabservice.service.GitLabService` - better to move `refreshDatabaseCache` method to another Spring-managed service class, and inject it.
- Lack of any logging.
- Lack of tests.

### What not to forget for production
- Using `spring.jpa.hibernate.ddl-auto=update` – in production, it should be `none` or `validate`.
- `spring.jpa.show-sql=true` – only for testing purposes, not for production.
- For production, I recommend using `.env` files and environment variables instead of specifying, for example, database credentials directly in `application.properties` and `docker-compose.yml`.
- For resolving dependencies, use a private Nexus repository – in case the libraries or specific versions are no longer available on Maven Central.
