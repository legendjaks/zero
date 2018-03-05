# zero
Zero down-time with Spring Cloud &amp; Docker Swarm

## Build Docker Images
For each project execute

> ./bdi

## Create Docker Swarm 

> docker swarm init

> docker network create -d overlay --attachable zero


## Run the Services
Configure the mount path wrt your choice and make sure the directory exists.

```
docker service rm config
docker service create  \
  --network zero --endpoint-mode vip --detach \
  --health-start-period 2m --health-interval 1m --health-retries 3 --health-timeout 5s --health-cmd "curl -f http://localhost:8888/actuator/health" \
  --update-order "start-first" --update-delay 2m --update-failure-action "rollback" \
  --restart-condition "on-failure" --stop-grace-period 1m \
  --replicas 1 \
  --mount type=bind,source=/Users/jay/Source/zero/logs,destination=/logs \
  --env L_PATH="/logs" \
  --hostname "config" \
  --name config config:1

docker service rm registry 
docker service create  \
  --network zero --endpoint-mode vip --detach \
  --health-start-period 2m --health-interval 1m --health-retries 3 --health-timeout 5s --health-cmd "curl -f http://localhost:8761/actuator/health" \
  --update-order "start-first" --update-delay 2m --update-failure-action "rollback" \
  --restart-condition "on-failure" --stop-grace-period 1m \
  --replicas 1 \
  --mount type=bind,source=/Users/jay/Source/zero/logs,destination=/logs \
  --env L_PATH="/logs" \
  --hostname "registry" \
  --name registry reg:1

docker service rm gateway 
docker service create  \
  --network zero --publish 8080:8080 --endpoint-mode vip --detach \
  --health-start-period 2m --health-interval 1m --health-retries 3 --health-timeout 5s --health-cmd "curl -f http://localhost:8080/actuator/health" \
  --update-order "start-first" --update-delay 2m --update-monitor 2m --update-failure-action "rollback" \
  --restart-condition "on-failure" --stop-grace-period 1m \
  --replicas 1 \
  --mount type=bind,source=/Users/jay/Source/zero/logs,destination=/logs \
  --env L_PATH="/logs" \
  --name gateway gateway:1

docker service rm edge 
docker service create  \
  --network zero --endpoint-mode dnsrr --detach \
  --health-start-period 2m --health-interval 1m --health-retries 3 --health-timeout 5s --health-cmd "curl -f http://localhost:8000/actuator/health" \
  --update-order "start-first" --update-delay 2m --update-monitor 2m --update-failure-action "rollback" \
  --restart-condition "on-failure" --stop-grace-period 1m \
  --replicas 2 \
  --mount type=bind,source=/Users/jay/Source/zero/logs,destination=/logs \
  --env L_PATH="/logs" \
  --name edge edge:1

docker service rm micro 
docker service create  \
  --network zero --endpoint-mode dnsrr --detach \
  --health-start-period 2m --health-interval 2m --health-retries 3 --health-timeout 5s --health-cmd "curl -f http://localhost:3000/actuator/health" \
  --update-order "start-first" --update-delay 2m --update-monitor 2m --update-failure-action "rollback" \
  --restart-condition "on-failure" --stop-grace-period 1m \
  --replicas 2 \
  --mount type=bind,source=/Users/jay/Source/zero/logs,destination=/logs \
  --env L_PATH="/logs" \
  --name micro micro:1
  ```

## Break It

For edge level test

> curl -X GET http://localhost:8080/edge/info?seq=10000

For micro level test

> curl -X GET http://localhost:8080/edge/micro/info?seq=10000

Change the parameter 'seq' to test timeout and various error status.

