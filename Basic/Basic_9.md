이전 글

[\[Spring\] 스프링 핵심 원리 - 기본편(8)](https://kkkkang1009.tistory.com/57)

## **빈 스코프**

### **빈 스코프란**

스프링 빈은 싱글톤 스코프를 디폴트 값으로 생성

싱글톤 : 기본 스코프, 스프링 컨테이너 시작과 끝까지 유지되는 가장 넓은 범위의 스코프

프로토타입 : 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리 X

웹 관련 스코프 : 

  - request : 웹 요청이 들어오고 나갈때까지 유지되는 스코프

- session : 웹 세션이 생성되고 종료될 때까지 유지되는 스코프

- application : 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프

### **프로토타입 스코프**

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbhrRS7%2Fbtr7hu1wKx9%2FpLKHCrLeY9D0qBanJRDIQK%2Fimg.png)

싱글톤 스코프 빈의 경우 조회시 항상 같은 인스턴스의 스프링 빈을 반환

프로토타입 스코프 빈의 경우 항상 새로운 인스턴스를 생성하여 반환

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FojP10%2Fbtr7xULwvaW%2FM6KR8wRxbdUq5llO7u1fiK%2Fimg.png)

1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청

2. 스프링 컨테이너는 프로토타입 빈을 생성하고, 필요 의존관계를 주입

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fd8P5K6%2Fbtr7iNzNJBu%2FSwk9KKPAaHc2GbkeqItPv0%2Fimg.png)

3. 스프링 컨테이너는 생성한 프로토타입 빈을 클라이언트에 반환

4. 이후 동일 요청에 항상 새로운 프로토타입 빈을 생성하여 반환

**스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계 주입, 초기화까지만 처리**

빈을 관리할 책임은 빈을 받은 클라이언트에 존재, @PreDestory 같은 종료 메서드 호출 불가, 클라이언트가 직접 호출해야 한다.

### **프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점**

싱글톤 빈과 함께 사용시 의도

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FuJNqn%2Fbtr7hUTItiK%2FyW5HvzCfNpg1F2G6b4eYuk%2Fimg.png)

1. 클라이언트A는 스프링 컨테이너에 프로토타입 빈을 요청

2. count를 1로 설정

3. 클라이언트B도 동일 트로토타입 빈을 요청

4. 프로토타입 빈 신규 생성 후 전달

5. 서로 각각 다른 인스턴스 

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fcztgjf%2Fbtr7g7MyENz%2F9oG4Lzoi4NmjDY3Hb7da0k%2Fimg.png)

clientBean은 싱글톤이므로 스프링 컨테이너 생성 시점에 생성되고, 의존 관계도 주입된다.

1. clientBean은 의존관계 자동 주입을 사용한다. -> 주입 시점에 스프링 컨테이너에 프로토타입 빈을 요청한다.

2. 스프링 컨테이너는 프로토타입 빈을 생성하여 반환하여 clientBean은 내부 필드에 보관한다.

3. 클라이언트A는 clientBean.logic()을 호출한다.

4. clientBean은 prototypeBean의 addCount를 호출하여 count이 증가한다. (1)

5. 클라이언트B가 clientBean.logic()을 호출한다.

6. clientBean은 prototypeBean의 addCount를 호출하여 count이 증가한다. (2)

clientBean이 내부에 가지고 있는 프로토타입 빈은 **이미 과거에 주입이 끝난 빈으로 사용할 때마다 새로 생성되는 것이 아님**

싱글톤 빈이 내부에 가지고 있는 프로토타입 빈은 싱글톤 빈과 같이 유지된다.

### **프로토타입 스코프 - 싱글톤 빈과 함께 사용시 Provider로 문제 해결**

ApplicationContext를 가지고 ac.getBean()을 통해 항상 새로운 프로토타입 빈을 생성

의존관계 주입 DI이 아니라 DL(Dependency Lookup) 의존관계 조회(탐색)이라고 한다.

**ObjectFactory, **ObjectProvider****

getObject() 함수를 호출하여 스프링 컨테이너를 통해 해당 빈을 찾아 반환

ObjectFactory : 기능이 단순, 별도의 라이브러리 필요 없음, 스프링에 의존

ObjectProvider : ObjectFactory 상속, 옵션,스트림 처리등 편의 기능 제공, 별도 라이브러리 필요 없음, 스프링에 의존

**JSR-330 Provider (javax.inject)**

Provider, get() 함수를 호출하여 스프링 컨테이너를 통해 해당 빈을 찾아 반환

java 표준이고, 기능이 단순, 별도의 라이브러리가 필요

### **웹 스코프**

웹 스코프는 웹 환경에서만 동작, 스프링이 종료시점까지 관리한다.

request : HTTP 요청 하나가 들어오고 나갈때까지 유지되는 스코프

session : HTTP session과 동일한 생명주기의 스코프

application : 서블릿 컨텍스트(ServletContext)와 동일한 생명주기의 스코프

websocket : 웹 소켓과 동일한 생명주기의 스코프

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FM5MO9%2Fbtr7s24f6D3%2FD0K9R5uab6R2oZr6fUubI0%2Fimg.png)

### **request 스코프 예제 만들기**

spring-boot-starter-web 라이브러리를 추가

AnnotationConfigServletWebServerApplicationContext를 기반으로 애플리케이션을 구동한다.

공통 포맷 : \[UUID\]\[reqeustURL\]{message}

MyLogger.java

```
@Component
@Scope(value = "request")
public class MyLogger {
    private String uuid;
    private String requestURL;
	''' '''
}
```

LogDemoController.java

```
@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request){
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");

        return "OK";
    }
}
```

LogDemoService.java

```
@Service
@RequiredArgsConstructor
public class LogDemoService {
    private final MyLogger myLogger;
    public void logic(String id) {
        myLogger.log("service id = " + id);
    }
}
```

위의 코드로 동작시 LogDemoController, LogDemoService가 생성되고 의존관계 주입받을 시점에 MyLogger (request 스코프)가 존재하지 않아 ScopeNotActiveException 에러가 발생된다. Scope 'request' is not active for the current thread; 

    -> 의존관계 주입 시점을 request 요청이 오는 시점으로 변경해야 한다.

### **스코프와 Provider**

LogDemoController, LogDemoService의 MyLogger에 ObjectProvider를 적용한다.

그 후 myLogger 사용 전 ObjectProvider에서 MyLogger를 가져오는 코드를 추가한다. 

```
// private final MyLogger myLogger;
private final ObjectProvider<MyLogger> myLoggerObjectProvider;

MyLogger myLogger = myLoggerObjectProvider.getObject();
```

### **스코프와 프록시**

@Scope에 proxyMode = ScopedProxyMode.TARGET\_CLASS 를 추가한다.

```
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
	''' '''
}
```

ObjectProvider를 적용하기 전 코드에서도 정상 동작이 된다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbZuC1K%2Fbtr7gg3Xqox%2FiC0Sme7vG6htfPyPhtGs70%2Fimg.png)

CGLIB 라이브러리를 통해 MyLogger를 상속받은 가짜 프록시 객체를 만들어 주입한다.

가짜 프록시 객체는 요청이 오면 진짜 빈을 요청하는 위임 로직이 들어있다.

해당 객체를 사용하는 클라이언트 입장에서는 원본인지 아닌지 모르게 동일하게 사용 가능(다형성)

Provider, proxyMode의 핵심 아이디어는 객체 조회를 필요한 시점까지 지연처리 하는 것

애노테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다

다형성과 DI 컨테이너가 가진 큰 강점이다.

주의점

- 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 주의해서 사용할 필요가 있다.

- 특별한 scope는 꼭 필요한 곳에만 최소화하여 사용할 필요가 있다. (유지보수가 어렵기 때문)