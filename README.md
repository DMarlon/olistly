# Nome Do Projeto

Olistly - Encurtador de URL

## 🔧 Funçoes

### Função 01:
- Cadastro de usuários

### Função 02:
- Encurtador de URL

### Função 03:
- Contador de visualização dos links gerados
- Visualização das datas e informações das visitas

## Dependencias 

[![Docker](https://img.shields.io/badge/Docker-0395bf?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Shell Script](https://img.shields.io/badge/Shell%20Script-000000?style=for-the-badge&logo=shell&logoColor=white)](https://pt.wikipedia.org/wiki/Shell_script/)

# Execução Do Projeto

1. Para iniciar as configurações rode o script [initial-configuration.sh](initial-configuration.sh)
   * Após isso será criado a pasta **api** no raiz do seu projeto com os arquivos **application.properties**, **cryptography.properties** e **email.properties**
2. Deve ser preenchido as informações de cada arquivo.
3. Sugestão de preenchimento:
   * application.properties
   
    ```
	spring.datasource.url=jdbc:postgresql://postgres/olistly
	spring.datasource.driver-class-name=org.postgresql.Driver
	spring.datasource.username=dev
	spring.datasource.password=dev
	spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
	spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
	spring.jpa.hibernate.ddl-auto=none

	spring.jackson.mapper.accept_case_insensitive_enums = true 
	spring.jackson.deserialization.read-enums-using-to-string = true

	server.error.include-message=always
	server.error.include-stacktrace=never

	logging.file.name=log/cli.log
	logging.level.web=ERROR
	logging.level.sql=ERROR

	system.name=CompanyName
	system.homepage=https://www.companyname.com.br/
	system.frontendurl=http://url.real-frontend.com.br
	system.backendurl=http://url.real-backend.com.br
	system.contact=contact@companyname.com.br
	system.noreply=noreply@companyname.com.br
	
	email.type=Local
	jwt.secret=KeyOf32bytesOr256bitssssssssssss

	#To configuration header Access-Control-Allow-Origin
	#WARNING Asterisc allowed all
	frontend.server=*
    ```
    
   * cryptography.properties
   
    ```
	crypto.salt=KeyOf32bytesOr256bitssssssssssss
	crypto.iv=KeyOf32bytesssss
	crypto.secret=KeyOf32bytesssss
    ```
    
   * email.properties
   
    ```
	user=youremail@gmail.com
	password=your_password
	mail.smtp.auth=true
	mail.smtp.host=smtp.gmail.com
	mail.smtp.port=587
	mail.smtp.starttls.enable=true
	mail.smtp.ssl.trust=smtp.gmail.com
    ```
    
4. Agora basta rodar o script [build.sh](build.sh)
5. Após o build basta rodar o comando **docker-compose up**


# Testes

1. Para testar a API pode ser usado o aplicativo de requisições que mais lhe agrade.
   * Porém tem o arquivo [api_test_insomnia.json](api_test_insomnia.json) que pode ser importado no Insomnia para facilitar os testes.
   * Para alterar as variaveis de ambiente basta usar o atalho **ctrl+e**, onde poderá trocar o valor da URL e o Token

## Feito Com:
[![UBUNTU](https://img.shields.io/badge/Ubuntu-e95420?style=for-the-badge&logo=ubuntu&logoColor=white)](https://ubuntu.com/download)
[![JAVA](https://img.shields.io/badge/Java-cc0000?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![ECLIPSE](https://img.shields.io/badge/Eclipse-2c2255?style=for-the-badge&logo=eclipse&logoColor=white)](https://www.eclipse.org/downloads/)
[![SPRING](https://img.shields.io/badge/Spring-6db33f?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![FLAYWAY](https://img.shields.io/badge/Flyway-cc0000?style=for-the-badge&logo=flyway&logoColor=white)](https://flywaydb.org/)
[![AUTH0](https://img.shields.io/badge/Auth0-000000?style=for-the-badge&logo=auth0&logoColor=white)](https://auth0.com/)
[![INSOMNIA](https://img.shields.io/badge/Insomnia-6600d8?style=for-the-badge&logo=insomnia&logoColor=white)](https://insomnia.rest/)

## 🔖 Licensa
[![LICENSA](https://img.shields.io/badge/Custom_GPL_3.0-E58080?style=for-the-badge&logo=bookstack&logoColor=white)](/LICENSE)

### Suporte Ou Contato

[![GITHUB](https://img.shields.io/badge/Github-000000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/dmarlon/)
[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/marlon-dauernheimer-55278073/)

### Documentação de Referência
Para referência adicional, considere as seguintes seções:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#using-boot-devtools)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-security)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Flyway DB](https://flywaydb.org/documentation/)

