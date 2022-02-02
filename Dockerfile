FROM openjdk:12

EXPOSE 8080
WORKDIR /api

COPY api/olistly.jar /api/olistly.jar

COPY api/email.properties /api/email.properties
COPY api/application.properties /api/application.properties
COPY api/cryptography.properties /api/cryptography.properties

CMD [\
  "java",\
  "-Dspring.config.location=/api/application.properties",\
  "-jar",\
  "olistly.jar"\
]

# Caso queira usar o gitlab e setar variaveis de ambiente, pode ser usado ARG e ENV e alterado os .properties para ler das variaveis de ambiente
#ARG arg_name
#ENV ENV_NAME=${arg_name}
