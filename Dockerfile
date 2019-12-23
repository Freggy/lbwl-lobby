FROM maven:3.6.3-jdk-8-slim AS builder

COPY . .
RUN mvn package

FROM freggyy/paperspigot:1.8.8

# TODO: pull lobby from cloud storage
COPY lobby_map /opt/spigot/lobby
COPY config/* /opt/spigot/
COPY --from=builder target/lobby.jar /opt/spigot/plugins
