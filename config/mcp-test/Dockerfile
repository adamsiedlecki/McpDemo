FROM eclipse-temurin:21-jdk-jammy

WORKDIR /

RUN mkdir -p /logs

RUN groupadd -g 1002 jenkins \
 && useradd -u 1002 -g 1002 jenkins

RUN chown -R jenkins:jenkins /logs
USER jenkins

ADD McpDemo.jar McpDemo.jar

CMD java -jar McpDemo.jar