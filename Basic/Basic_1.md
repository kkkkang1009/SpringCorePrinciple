## **객체 지향설계와 스프링**

### **1\. 이야기 - 자바 진영의 추운 겨울과 스프링의 탄생**

기존 EJB(Enterprise Java Beans) 사용  but 비싼 비용, 복잡하고 어려움, 느림

Spring -> EJB 컨테이너 대체, 단순함

Hibernate -> EJB 엔티티빈 기술 대체, JPA(Java Persistence API) 표준 정의

### **2\. 스프링이란?**

필수 : 스프링 프레임워크, 스프링 부트

선택 : 스프링 데이터, 스프링 세션, 스프링 시큐리티, 스프링 REST Docs, 스프링 배치, 스프링 클라우드

핵심 : 객체 지향 언어인 자바 언어 기반의 프레임워크

**스프링 프레임워크**

핵심 기술 : 스프링 DI 컨테이너, AOP, 이벤트, 기타

웹 기술 : 스프링 MVC, 스프링 WebFlux

데이터 접근 기술 : 트랜잭션, JDBC, ORM 지원, XML 지원

기술 통합 : 캐시, 이메일, 원격접근, 스케쥴링

테스트 : 스프링 기반 테스트 지원

언어 : 코틀린, 그루비

**스프링 부트**

단독 실행할 수 있는 스프링 어플리케이션 쉽게 생성

내장 Tomcat으로 별도 웹 서버 설치 필요 X

빌드 구성을 위한 starter 종속성 제공

스프링, 3rd party 라이브러리 자동 구성

메트릭, 상태 확인, 외부 구성 같은 프로덕션 준비 기능 제공

### **3\. 좋은 객체 지향 프로그래밍이란?**

**객체 지향 프로그래밍**

객체들의 모임, 각각의 객체는 메세지를 주고 받으며, 데이터 처리한다.

다형성(Polymorphism) : 프로그램이 유연하고 변경이 용이하다.

Ex) 운전자 - 자동차(아반떼, 소나타, 테슬라), 로미오(장동건, 원빈) - 줄리엣(김태희, 송혜교)

역할(인터페이스)과 구현(구현 객체)으로 구분하면 단순하고 유연해지며 변경이 편리하다.

-   인터페이스만 알면된다.
-   내부 구조를 몰라도 된다.
-   내부 구조가 변경되도 영향 없다

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdgT4dX%2FbtrWfQgs5dd%2FtgYXtdkK9NjIsO0S4TGf4k%2Fimg.png)
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FnhET8%2FbtrWd13Rvmo%2FAaZKj94Sbtgzo81mbtbOhK%2Fimg.png)

**스프링과 객체 지향**

제어의 역전(IoC), 의존관계 주입(DI)은 다형성을 활요해서 역할과 구현을 편리하게 다룰 수 있도록 지원한다.

### **4\. 좋은 객체 지향 설계의 5가지 원칙(SOLID)**

SRP(Single Responseibility Principle) 단일 책임 원칙 : 한 클래스는 하나의 책임만 가져야 한다.

OCP(Open/Closed Principle) 개방/폐쇄 원칙 : 확장에는 개방적이나, 변경에는 폐쇄적이여야 한다.

LSP(Liskov Substitution Principle) 리스코프 치환 원칙 : 정확성을 깨지 않으며 하위 타입의 인스턴스를 바꿀 수 있어야 한다.

ISP(Interface Segregation Principle) 인터페이스 분리 원칙 : 특정 클라이언트를 위한 여러 개의 인스턴스가 범용 인터페이스 하나보다 낫다.

DIP(Dependency Inversion Principle) 의존관계 역전 원칙 : 구현 클래스가 아닌 인터페이스에 의존하여야한다.

다형성 만으로는 OCP, DIP를 지킬 수 없다.

Ex)

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F3Ghd1%2FbtrWc1pMCmv%2F7X2vvooSDgAYaSF2ZShEuk%2Fimg.png)
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FkZh6W%2FbtrWnoC7R4e%2FTHvbqZyMF7DbzFLj63uUfk%2Fimg.png)

### **5\. 객체 지향 설계와 스프링**

스프링은 DI로 다형성 + OCP, DIP 원칙을 지킨다.

다음 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (2)](https://kkkkang1009.tistory.com/46)
[\[Spring\] 스프링 핵심 원리 - 기본편 (3)](https://kkkkang1009.tistory.com/50)
[\[Spring\] 스프링 핵심 원리 - 기본편 (4)](https://kkkkang1009.tistory.com/52)