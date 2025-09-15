# GitHub & GitLab Service
This service aggregates user information and public repositories from both GitHub and GitLab platforms.
The main service, `github-gitlab-service`, calls the `gitlab-service` to fetch information.

It exposes an API endpoint:
```
http://localhost:8087/api/users/{username}
```
Replace `{username}` with the desired GitHub/GitLab username. Default port is `8087`. HTTP method is `GET`.


More details about the `github-gitlab-service` can be found in its [README](../README.md).

More details about the `gitlab-service` can be found in its [README](../gitlab-service/README.md).
