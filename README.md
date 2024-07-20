# Rest with Spring Boot and Java
[![Docker Hub Repo](https://img.shields.io/docker/pulls/eduardokaio/rest-with-spring-boot-erudio.svg)](https://hub.docker.com/repository/docker/eduardokaio/rest-with-spring-boot-erudio)

Este projeto é uma API RESTful desenvolvida com Spring Boot e Java, com várias funcionalidades e boas práticas incorporadas.

## Características

- **API RESTful**
- **Tratamento de Exceptions**
- **Integração ao Banco de Dados: MySQL**
- **Padrão de Projetos: Value Object**
- **Content Negotiation**: JSON, XML, YAML
- **HATEOAS**
- **Documentação**: Swagger
- **Configuração de CORS**
- **Autenticação com JWT e Spring Security**
- **Download e Upload de arquivos**
- **Paginação**
- **Testes unitários e de integração automatizados**: JUnit, Rest Assured, TestContainers
- **Dockerização**
- **Integração contínua**: Github Actions

## Pré-requisitos

- Java 11 ou superior
- Maven
- Docker (opcional, para uso de contêineres)
- MySQL

## Configuração do Projeto

### Clonar o Repositório

```sh
git clone https://github.com/eduardokaio/rest-with-spring-boot-erudio.git
cd rest-with-spring-boot-and-java
```

### Configurar o Banco de Dados
1. Crie um banco de dados MySQL.
2. Configure as credenciais do banco de dados no application.yml:

```sh
url: jdbc:mysql://localhost:3306/seu_banco_de_dados?useTimezone=true&serverTimezone=UTC
username: seu_usuario
password: sua_senha
```

### Executar o Projeto
```sh
./mvnw spring-boot:run
```

### Uso do Docker

Para construir e executar o projeto em um contêiner Docker:
```sh
docker build -t rest-with-spring-boot-erudio .
docker run -p 8080:8080 rest-with-spring-boot-erudio
```

### Documentação da API

A documentação da API está disponível no Swagger:
```bash
http://localhost:8080/swagger-ui.html
```

### Testes

Para executar os testes:
```sh
./mvnw test
```

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`).
4. Faça um push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Distribuído sob a licença MIT. Veja `LICENSE` para mais informações.

---

Feito com ❤️ por Kaio
