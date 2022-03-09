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
* Log File Out

## 해결 과제
### 로그 포멧팅은 어떻게 처리할 것인가 ?
(정리 필요)
### 로그 유형을 Action, Service, Error 로 어떻게 분리할 것인가 ?
(정리 필요)
### 로그를 추적할 경우 하나의 Key 를 통해 로그 추적이 가능 한가 ?
(정리 필요)
### Exception 의 경우 로그 추적 방식은 어떻게 되는가 ?
(정리 필요)
### MSA 처럼 분산된 서비스의 경우 Transaction Key 해결 방법은 ? 
(Token 확인이 어려울 경우 고민)
(고민 필요)
### Java Application 로그 설정은 유지하면서 ELK Stack 을 연결 가능 한가 ?
(고민 필요)
### ELK Stack 이 대용량 트래픽을 견딜수 있는 구조인가 ?
(확인 필요)