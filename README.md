# Cardmarket backend
[![CircleCI](https://circleci.com/gh/happyharbor/cardmarket-back.svg?style=shield)](https://circleci.com/gh/circleci/circleci-docs)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=happyharbor_cardmarket-back&metric=coverage)](https://sonarcloud.io/summary/new_code?id=happyharbor_cardmarket-back)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=happyharbor_cardmarket-back&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=happyharbor_cardmarket-back)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=happyharbor_cardmarket-back&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=happyharbor_cardmarket-back)
[![GitHub license](https://img.shields.io/github/license/happyharbor/cardmarket_back)](https://github.com/happyharbor/cardmarket_back/blob/master/LICENCE)

## Service Oriented Architecture
Each module should have a specific role. Login modules are handling the login,
client is handling the interaction with the cardmarket API, cardmarket-core is handling 
the business (probably needs to break down), rest exposes endpoint of core modules

## Spring Security
Project used jwt token based authentication. It also uses cors protection and
Role based access endpoints

## Setting up development environment
1. Install postgres, with user/pass: postgres/postgres
2. Create an `application-secrets.yaml` in cardmarket-rest project, alongside with [application.yaml](cardmarket-rest/src/main/resources/application.yaml) with the following structure:
```yaml
security:
  jwt:
    token:
      secret-key: {a secret key with at least 64 characters}

client:
  credentials:
    app-token: { app-token from cardmarket }
    app-secret: { app-secret from cardmarket }
    access-token: { access-token from cardmarket }
    access-token-secret: { access-token-secret from cardmarket }

power-users: list of users that you would like to scan
```
3. Enable these spring active profiles: `dev, secrets` in the Run/Debug configuration of intellij
4. Enable liquibase hub in the VM Options: `-Dliquibase.hub.apiKey=vcDko4sK0lh99lGaYsOlUufV2bZiS1LxIGbIfbTJaJs`
5. Run the [CardmarketRestApplication](cardmarket-rest/src/main/java/io/happyharbor/cardmarket/rest/CardmarketRestApplication.java)
6. Execute the following SQL to add an admin user with credentials: `admin`/`Admin!10`
```postgresql
INSERT INTO public.application_user (id, create_ts, modify_ts, password, username) VALUES ('71c91ef7-edbd-4067-bd32-2bb27a3165ab', '2021-10-07 07:07:45.172916', '2021-10-07 07:07:45.172947', '$2a$12$sFaEA3yUCDkbLworGm6nMu4MSk6GCyBzJkJRL8s7604Vqz/hxtjNO', 'admin');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES ('71c91ef7-edbd-4067-bd32-2bb27a3165ab', 1);
```
6. Follow the relevant instructions of [cardmarket front](https://github.com/happyharbor/cardmarket_front)

## Support
This project is being supported by 
<img src="https://iconape.com/wp-content/png_logo_vector/cib-jetbrains.png" alt="jetbrains logo" width="20%"/>
