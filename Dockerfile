FROM docker.fream.pl/wildfly:16-adoptopenjdk-11-openj9

COPY target/hellopoland.war /opt/jboss/wildfly/standalone/deployments/
