이전 글

[\[Spring\] 스프링 핵심 원리 - 기본편(7)](https://kkkkang1009.tistory.com/56)

## **빈 생명주기 콜백**

### **빈 생명주기 콜백 시작**

DB 커넥션 풀, 네트워크 소켓 등 애플리케이션 시작 시점에 필요 연결을 미리 해두고, 종료 시점에 연결을 종료를 진행하려면, 객체의 초기화와 종료 작업이 필요하다.

**스프링 빈의 라이프 사이클**

**객체 생성 -> 의존관계 주입**

초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 호출 필요

스프링은 의존관계 주입이 완료되면 스프링 빈에 콜백 메서드를 통해 초기화 시점을 알려주는 다양한 기능 제공

스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다.

**스프링 빈의 이벤트 라이프사이클**

**스프링 시작 -> 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료**

**초기화 콜백** : 빈 생성 후 의존관계 주입이 완료된 후 호출

**소멸전 콜백** : 빈이 소멸되기 직전에 호출

**객체의 생성과 초기화를 분리하자!**

생성자는 필수 정보를 받고, 메모리를 할당하여 객체를 생성하는 책임

초기화는 생성된 값들을 활용하여 외부 커넥션을 연결하는 무거운 동작을 수행

### **인터페이스 InitializingBean, DisposableBean**

InitializingBean의 afterPropertiesSet()으로 초기화를 지원

DisposableBean의 destory()로 소멸을 지원

초창기 방법 현재는 사용 X

단점 :

- 스프링 전용 인터페이스로 코드가 스프링에 의존하게 된다.

- 초기화, 소멸 메소드 이름 변경 불가

- 외부 라이브러리에 적용 불가

```
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient implements InitializingBean, DisposableBean {
    ''' '''
	@Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메세지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }
}
```

### **빈 등록 초기화, 소멸 메서드**

@Bean(initMethod ="initMethodName", destroyMethod ="destroyMethodName")

메서드 이름을 자유롭게 설정

스프링 빈이 스프링 코드에 의존 안함

설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드 적용 가능

@Bean의 destroyMethod 속성의 Default는 (inferred)로 등록

-> close, shutdown 이름의 메소드를 자동 호출한다.

destroyMethod=""  적용시 소멸 메소드 미사용

```
public class NetworkClient {
    ''' '''
	public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
```

```
@Configuration
static class LifeCycleConfig {
    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClient networkClient(){
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http://hello-spring.dev");
        return networkClient;
    }
}
```

### **애노테이션 @PostConstruct, @PreDestory**

최신 스프링에서 권장하는 방법

javax 패키지로 스프링이 아닌 다른 컨테이너에서도 동작

컴포넌트 스캔과 잘 어울림

유일한 단점으로 외부 라이브러리에 적용 불가

```
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClient {
    ''' '''
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
```