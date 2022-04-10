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
(확인 필요)


