FROM gradle:jdk10 as builder

USER root
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

WORKDIR idm
RUN gradle build
RUN java -jar build/libs/com.mn.service.idm-all.jar -c config.yaml

WORKDIR inventory
RUN gradle build
RUN java -jar build/libs/com.mn.service.inventory-all.jar -c config.yaml

WORKDIR billing
RUN gradle build
RUN java -jar build/libs/com.mn.service.billing-all.jar -c config.yaml

WORKDIR gateway
RUN gradle build
RUN java -jar build/libs/com.mn.service.gateway-all.jar -c config.yaml

