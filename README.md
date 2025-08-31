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
