# Spring Boot + ELK stack
Spring Boot Project 와 ELK stack 을 연동 해서 Log 통합 시스템을 만드는 프로젝트

## Environment
### Project
* Java 8
* Spring Boot 2.6.4
* Spring Boot Web
* Spring Boot AOP
* Spring Data JPA
* Lombok
### ETC
* Jmeter
* PostMan
* Docker
* ELK Stack 7.16.2


### TODO
* 서버별 구분값 필요
* 불필요한 값들 Filter 로 제거 필요

---
# ELK Stack 이란 무엇인가요 ?
ELK 는 Elasticsearch, Logstash, Kibana 세가지 오픈 소스 이니셜 입니다.
# Elasticsearch ?
Elasticsearch 는 Json 기반의 분산형 RESTful 검색 및 분석 엔진입니다.

Apache Lucene 기반으로 구축되었으며, 분산형 및 개방형을 특징으로 합니다.

Lucene이 Java 로 개발되었기 때문에 Elasticsearch 또한 Java 로 개발 되었습니다.

## Elasticserach 를 왜 사용하나요 ?

일반적으로 흔히 사용하는 RDB의 경우 비 정형 데이터를 색인하고 검색하는 것이 불가능한데 반해, Elasticsearch 는 비 정형 데이터를 색인하고 검색하는 것이 가능합니다. 또한, Elasticsearch 의 장점은 역색인 구조로 빠른 검색속도를 자랑합니다.

## Elasticsearch 는 어디에 사용하나요 ?

- 애플리케이션 검색
- 웹사이드 검색
- 로깅과 로그 분석
- 인프라 메트릭, 컨테이너 모니터링
- 기타

# Kibana ?

Elastic Stack 기반으로 구축된 오픈 소스 FE 애플리케이션으로, Elasticsearch 에서 색인된 데이터를 검색하여 분석 및 시각화 하는 기능 플랫폼입니다. 고급 데이터 분석을 쉽게 수행하고 다양한 차트, 테이블 및 맵에서 데이터를 시각화 가능합니다. Kibana는 Elasticsearch 의 결과를 보여주는 역할을 하고 있습니다.

## Kibana 의 주요 기능

- Discover

  Elasticsearch 에 저장된 데이터를 한눈에 확인할 수 있으며, IP, URL 등의 키워드를 통해 조회한 키워드를 저장하여 다시 불러올 수 있습니다. 데이터를 Set을 Raw한 Json 형태로 확인이 가능하며 Raw 데이터를 전부 볼 수도 있고, 원하는 데이터만 조회하여 볼 수도 있습니다.

- Visualize

  Elasticsearch에서 수집된 데이터 및 정보를 시각화하는 페이지 입니다. 여러 종류의 차트를 지원하고 있으며 차트에 표현할 수 있는 X축, Y축의 값을 설정할 수 있습니다.

- DashBoard

  Visualize를 통하여 시각화한 객체를 모아서 하나의 DashBoard에 배치하여 한눈에 확인이 가능합니다. DashBoard는 저장도 가능하고 URL을 통하여 배포도 가능합니다. DashBoard와 Visualize는 Share를 통하여 공유 또한 가능합니다.

- Setting

  Kibana의 설정을 통하여 새로운 index를 추가하고 Visualize, DashBoard에서 생성한 데이터를 수정 및 삭제할 수 있는 영역입니다.


# Logstash ?

Logstash는 실시간 파이프 라인 기능을 가진 오픈소스 데이터 수집 엔진 입니다.

Logstash는 Server-side 데이터 처리 파이프 라인으로 다양한 소스에서 동시에 데이터를 수집하고 통합합니다. 또한, 수집된 데이터를 정규화하여 Elasticsearch등의 목적지로 전송하는 역할을 합니다. 거의 대부분의 이벤트를 수집하여 변환 할 수 있으며, 기본으로 제공되는 여러 코덱을 이용해 수집 프로세스를 간소화할 수 있습니다.

# Spring Boot + ELK Stack

간단하게 Spring Boot 에 ELK Stack 을 적용하여 Log 를 분석하는 예제를 다뤄보겠습니다.

먼저 변경전 로그를 분석할때 상황과 ELK Stack을 적용하여 로그를 분석할때를 비교하여 보겠습니다.


기존의 로그 확인 절차

현재 기존의 원하는 로그를 찾기 위해 개발자는 각각의 서버에 접속 하여 Log 파일을 모두 열어 원하는 데이터를 찾으려 할 것입니다.


ELK Stack 을 적용한 로그 확인 절차

ELK Stack 을 적용하여 전과는 다르게 원하는 로그를 찾기 위해 개발자는 각각의 서버에 접속하는것이 아닌 데이터 파이프 라인인 Logstash 를 통해 Elasticsearch 에 데이터를 저장하고 색인된 데이터를 Kibana 를 통해 보기 때문에 각각의 서버의 로그를 모두 찾는 수고를 덜었습니다.

## 예제

ELK Stack 을 Docker 로 빠르게 구성하여 띄울수 있도록 구성하였습니다.

### Elasticsearch Docker

```yaml
version: '3.8'
services:
  esm01:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.2
    container_name: esm01
    ports:
      - "9200:9200"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    environment:
      ES_SETTING_BOOTSTRAP_MEMORY__LOCK: "true"
      ES_JAVA_OPTS: "-Xmx512m -Xms512m -Xlog:disable"
      node.name: esm01
      node.roles: master
      network.host: 0.0.0.0
      cluster.name: es-cluster
      cluster.initial_master_nodes: esm01
      discovery.seed_hosts: esm01,esd01,esd02
      xpack.security.enabled: "false"
      xpack.monitoring.collection.enabled: "false"
  esd01:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.2
    container_name: esd01
    ulimits:
      memlock:
        soft: -1
        hard: -1
    environment:
      ES_SETTING_BOOTSTRAP_MEMORY__LOCK: "true"
      ES_JAVA_OPTS: "-Xmx512m -Xms512m -Xlog:disable"
      node.name: esd01
      node.roles: data,ingest
      cluster.name: es-cluster
      cluster.initial_master_nodes: esm01
      discovery.seed_hosts: esm01,esd01,esd02
      xpack.security.enabled: "false"
      xpack.monitoring.collection.enabled: "false"
  esd02:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.2
    container_name: esd02
    ulimits:
      memlock:
        soft: -1
        hard: -1
    environment:
      ES_SETTING_BOOTSTRAP_MEMORY__LOCK: "true"
      ES_JAVA_OPTS: "-Xmx512m -Xms512m -Xlog:disable"
      node.name: esd02
      node.roles: data,ingest
      cluster.name: es-cluster
      cluster.initial_master_nodes: esm01
      discovery.seed_hosts: esm01,esd01,esd02
      xpack.security.enabled: "false"
      xpack.monitoring.collection.enabled: "false"

volumes:
  data01:
    driver: local
  data02:
    driver: local
  data03:
    driver: local

networks:
  default:
    external:
      name: loggin
```

Elasticsearch 를 Cluster로 구성하여 데이터 저장과 상태 정보를 관리하는 역할을 분리하였습니다.

esm01 는 상태 정보를 관리하는 역할을 하며, esd01, esd02 는 데이터를 저장하는 역할을 합니다.

### Kibana Docker

```yaml
version: '3.8'
services:
  kibana:
    image: docker.elastic.co/kibana/kibana:7.16.2
    container_name: kibana
    ports:
      - "5601:5601"
    volumes:
      - ./config/kibana.yml:/usr/share/kibana/config/kibana.yml
    environment:
      NODE_OPTIONS: "--max-old-space-size=2048"

networks:
  default:
    external:
      name: loggin
```

```yaml
server.name: kibana
server.port: 5601
server.host: "0.0.0.0"

elasticsearch.hosts: ["Elasticsearch:PORT"]

monitoring.ui.container.elasticsearch.enabled: true
```

Kibana 를 띄우는 Docker Compose 입니다. Kibana 의 경우 Docker Compose 와는 별도로 Config 파일을 셋팅해주어야 합니다.

### Logstash Docker

```yaml
version: '3.8'
services:
  logstash:
    image: docker.elastic.co/logstash/logstash:7.16.2
    container_name: logstash
    volumes:
      - ./config/logstash.yml:/usr/share/logstash/config/logstash.yml
      - ./pipelines/logstash.conf:/usr/share/logstash/pipeline/logstash.conf
    ports:
      - "5044:5044"
      - "5000:5000/tcp"
      - "5000:5000/udp"
      - "9600:9600"
    environment:
      LS_JAVA_OPTS: "-Xmx512m -Xms512m"

networks:
  default:
    external:
      name: loggin
```

```yaml
http.host: "0.0.0.0"
```

```yaml
input {
  tcp {
    port => 5000
    codec => "json"
  }
}

output {
  elasticsearch {
    hosts => [{{Elasticsearch:PORT}}]
    ecs_compatibility => disabled
    index => "spring-logger-%{+YYYY.MM.dd}"
  }
}
```

Logstash 를 띄우는 Docker Compose 입니다. Logstash 의 경우 Docker Compose 와는 별도로 Config 와 Pipeline 파일을 셋팅해주어야 합니다.

### Spring Boot

Spring Boot 의 경우 Logback 설정을 통해 Logstash 로 데이터를 보내기위해 logstash-logback-encoder 라이브러리를 활용하여 구성하였습니다.

```yaml
implementation 'net.logstash.logback:logstash-logback-encoder:4.11'
```

build.gradle 파일에 Dependencies 를 추가해 주도록 합니다.

다음으로는 logback 설정을 바꿔줍니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="5 minutes" debug="false">
    <!-- console -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%magenta([%d{yyyy:MM:dd HH:mm:ss.SSS}]) %highlight([%5level]) [%thread] %-55cyan([%logger{36}:%L]) : %msg %n</pattern>
        </encoder>
    </appender>

    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>{{LogStash:PORT}}</destination>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <providers>
                <timestamp>
                    <timeZone>UTC</timeZone>
                </timestamp>
                <version/>
                <logLevel/>
                <threadName/>
                <loggerName/>
                <message />
            </providers>
            <includeMdcKeyName>Txid</includeMdcKeyName>
            <includeMdcKeyName>Method</includeMdcKeyName>
            <includeMdcKeyName>Uri</includeMdcKeyName>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="LOGSTASH"/>
    </root>
</configuration>
```

LOGSTASH 부분을 통해 데이터를 Json 형태로 가공해 logstash 로 데이터를 보내도록 셋팅 하였습니다.

이 예제 같은 경우 MDC를 이용 하여 Txid (Transaction ID), Method, Uri 등을 Custom 해서 사용하기 위해 별도로 추가 셋팅 하였습니다.

## 심화
해당 프로젝트는 예제 파일로서 실제로 운영하는 서버에 적용하는것은 문제가 있습니다.

대용량 트레픽과 관련된 문제 또는 아래 해결과제들을 각 서버에 맞춰 고민하여 해결해야 할것 같습니다.

## 해결 과제
### 로그 포멧팅은 어떻게 처리할 것인가 ?
해당 프로젝트에서는 LogStash Encoder 라이브러리를 사용하여 인코딩 하였으며, 
Transaction ID, Request Method , URI, Log Level 등 API 추적이 쉬운 값으로 검색할 수 있도록 구성 하였습니다. 
### 로그 유형을 Action, Service, Error 로 어떻게 분리할 것인가 ?
역할에 따라 로그를 분리하여 관리하기 위해서 계층을 나눠 관리하였다.

* Action Log : Request or Response Log 를 위한 계층입니다.
* Service Log : Service or Business Log 를 위한 계층입니다.
* Error Log : Error or Exception Log 를 위한 계층입니다.
### 로그를 추적할 경우 하나의 Key 를 통해 로그 추적이 가능 한가 ?
Transaction ID 의 값을 만들어 추적하는 방식을 사용하였습니다.

Java 의 UUID Class 의 randomUUID 함수 를 이용하여 유니크한 키값을 가지게 만들었습니다.

Transaction ID를 Logback 에 Setting 하기 위해 MDC 를 사용하여 Log 의 Transaction ID를 추가하였습니다.

Filter 를 통해 Transaction ID를 추가한 이유는 404, 405, 400... 등 400번대 에러 Log 에 Transaction ID를 넣어 핸들링 하기 위해 Filter 에 추가 하였습니다.
### Exception 의 경우 로그 추적 방식은 어떻게 되는가 ?
Exception 추적 방식은 Kibana 를 통해 Error 가 확인 되면 해당 Transaction ID 로 검색하여 빠른 Error 확인이 가능하도록 구성 하였습니다.
### MSA 처럼 분산된 서비스의 경우 Transaction Key 해결 방법은 ? 
분산 서비스의 경우 Header 에 Transaction Id 를 셋팅해 Transaction Id 를 주고 받는것으로 한다.

현재 프로젝트의 경우 Filter 를 통해 Transaction Id 를 셋팅하지만, <br>
Api Gateway 에서 셋팅 해서 Transaction Id 를 주고 받도록 한다.
### Java Application 로그 설정은 유지하면서 ELK Stack 을 연결 가능 한가 ?
Logback 설정을 통해서 Console 부분 File 부분 LogStash 부분으로 나뉘어서 기존의 Log 시스템에 영향을 주지 않고 사용 가능 하도록 작성이 가능하다.

### ELK Stack 에서 로그 삭제 기준을 어떻게 잡을 것인가?
ELK Stack 에서는 Elasticsearch Indices policy 정책을 설정하여 자동 삭제 되도록 설정하여 사용한다.
### ELK Stack 이 대용량 트래픽을 견딜수 있는 구조인가 ?
대용량 트래픽을 견디기 위해서는 Logstash 에서 데이터를 받는 부하를 줄이는게 먼저가 되어야 할것 같습니다.

logstash 앞에 kafka 를 두어 데이터가 유실되거나 logstash 의 부하를 1차적으로 줄일수 있는 방법이 있습니다.

또한, kafka 에 데이터를 적재할 경우 Spring Boot 에서 직접적으로 데이터를 쏘는것이 아닌, filebeat 를 중간에 두어
데이터를 보내는것이 더 좋을것으로 생각 되어 집니다.