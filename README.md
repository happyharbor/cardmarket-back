# Cardmarket backend

## Service Oriented Architecture
Each module should have a specific role. Login modules are handling the login,
client is handling the interaction with the cardmarket API, cardmarket-core is handling 
the business (probably needs to break up), rest exposes endpoint of core modules

## Spring Security
Project used jwt token based authentication. It also uses cors protection and
Role based access endpoints
