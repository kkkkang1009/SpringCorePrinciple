## **웹 애플리케이션 이해**

### **1\. 웹 서버, 웹 애플리케이션 서버**

웹 서버(Web Server)

-   정적 리소스 제공, 기타 부가기능(HTML, CSS, JS, 이미지, 영상 등)
-   Ex) NginX, Apache

웹 애플리케이션 서버(WAS - Web Application Server)

-   프로그램 코드를 실행해서 애플리케이션 로직 수행
-   Servlet, JSP, Spring MVC
-   Ex) Tomcat, Jetty, Undertow

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F8DvrE%2Fbtso0P9q7bR%2FpfEn9ySgFoKbTh71TsLg90%2Fimg.png)

웹 서버, 웹 애플리케이션 서버 차이

-   웹 서버는 정적 리소스, WAS는 애플리케이션 로직

웹 시스템 구성 - WEB, WAS, DB

-   웹 서버가 정적 리소스 처리
-   WAS는 중요 애플리케이션 로직 처리 전담
-   WAS, DB 장애시 WEB 서버가 오류 화면 제공 가능

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FG5QHZ%2Fbtso0ssXvZl%2FyD5VgI2gCgweSyPA6NrxZ0%2Fimg.png)

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbU7n3W%2Fbtspxqtzjr0%2FbxMRW5tGTWK2rfR3JmuDJk%2Fimg.png)

### **2\. 서블릿**

웹 통신 과정

1.  웹브라우저 -> /hello HTTP 요청
2.  WAS에서 Request 객체를 받아 서블릿 객체 호출
3.  개발자는 Req 객체에서 필요 정보 꺼내 사용
4.  개발자는 Resp 객체에 응답 정보를 입력
5.  WAS는 Response 객체로 Http 응답 생성

서블릿을 지원하는 WAS 사용 - 의미있는 비즈니스 로직 자동 처리

-   urlPatterns의 URL이 호출되면 서블릿 코드가 실행
-   HttpServletRequest, HttpsServletResponse를 사용하여 요청과 응답을 편리하게 사용

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FHtWPU%2FbtspsEZY0HF%2Fl5TGsIBliaOsSovvbT7dP0%2Fimg.png)

서블릿 컨테이너

-   Tomcat처럼 Servlet를 지원하는 WAS를 Servlet Container라 한다.
-   서블릿 객체 생성, 초기화, 호출, 종료 등 생명 주기를 관리한다.
-   서블릿 객체는 싱글톤으로 관리 - 공유 변수 사용 주의, 컨테이너 종료시 함께 종료
-   JSP도 서블릿으로 변환되어 사용
-   동시 요청을 위한 멀티 스레드 처리 지원

### **3\. 동시 요청 - 멀티 쓰레드**

쓰레드

-   코드를 순차적으로 실행하는 것은 쓰레드
-   쓰레드는 한번에 하나의 코드 라인만 수행
-   동시 처리가 필요하면 쓰레드를 추가로 생성

요청마다 쓰레드 생성시

-   cpu, mem 최대치까지 생성 가능
-   쓰레드 생성 비용 비쌈, 응답 속도 지연 가능
-   컨텍스트 스위칭 비용 발생
-   쓰레드 생성 제한 없음

쓰레드 풀

-   쓰레드 풀에 보관하고 관리한다. (tomcat은 200개 default)
-   최대 쓰레드 사용중일 경우 거절 혹은 대기 설정 가능
-   쓰레드 생성, 종료 비용 절약 가능
-   많은 요청이 들어와도 기존 요청은 안전하게 처리 가능

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcXP8zd%2Fbtspol0N54J%2FvDnwhnDK3BWwSYAmFfOwk1%2Fimg.png)

쓰레드 풀 적정 숫자

-   애플리케이션 로직의 복잡도, CPU, 메모리, I/O 리소스 상황에 따라 다름
-   성능 테스트 - 아파치 ab, 제이미터, nGrinder

WAS의 멀티 쓰레드 지원

-   개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨
-   멀티 쓰레드 환경이므로 싱글톤 객체는 주의해서 사용

### **4\. HTML, HTTP API, CSR, SSR**

정적 리소스

-   고정된 HTML, CSS, JS, 이미지, 영상 등을 제공

HTML 페이지

-   동적으로 필요한 HTML 파일을 생성(JSP, 타임리프 등)해서 전달

HTTP API

-   데이터를 전달(주로 JSON 형식 사용)
-   UI화면이 필요하면, 클라이언트가 별도 처리
-   웹클라이언트 to 서버, 앱클라이언트 to 서버, 서버 to 서버

SSR(서버 사이드 렌더링)

-   HTML 최종 결과를 서버에서 만들어 웹 브라우저에 전달
-   주로 정적인 화면에 사용
-   JSP, 타임리프 

CSR(클라이언트 사이드 렌더링)

-   HTML 결과를 JS를 이용해 동적으로 생성해 적용
-   동적인 화면에 사용, 필요한 부분부분 변경할 수 있음
-   React, Vue.js 

### **5\. 자바 백엔드 웹 기술 역사**

#### **자바 웹 기술**

서블릿 - 1997

-   HTML 생성이 어려움

JSP - 1999

-   비즈니스 로직까지 너무 많은 역할 담당

서블릿, JSP 조합 MVC 패턴 사용

-   모델, 뷰, 컨트롤러 역할을 나우어 개발

MVC 프레임워크 - 2000년 초 ~ 2010년 초

-   MVC 패턴 자동화, 
-   스트럿츠, 웹워크, 스프링 MVC

애노테이션 기반의 스프링 MVC 등장

-   @Controller

스프링 부트의 등장

-   서버를 내장
-   기존에는 서버에 WAS를 직접 설치하고, 소스는 War 파일을 만들어서 설치한 WAS에 배포
-   스프링 부트는 빌드결과(Jar)에 WAS 서버 포함 -> 빌드 배포 단순화

Web Servlet - Spring MVC

Web Reactive - Spring WebFlux

-   비동기 넌 블러킹 처리
-   최소 쓰레드로 최대 성능 - 쓰레드 컨텍스트 스위칭 비용 효율화
-   함수형 스타일로 개발 - 동시처리 코드 효율화
-   서블릿 기술 사용 X

#### **자바 뷰 템플릿 역사**

JSP 

-   속도 느림, 기능부족

프리마커, Velocity

-   속도 문제 해결, 다양한 기능

타임 리프(Thymeleaf)

-   내추럴 템플릿 : HTML 모양을 유지하면서 뷰 템플릿 적용 가능
-   스프링 MVC와 강력한 기능 통합