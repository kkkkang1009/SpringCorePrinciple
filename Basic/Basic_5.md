[\[Spring\] 스프링 핵심 원리 - 기본편 (1)](https://kkkkang1009.tistory.com/44)

[\[Spring\] 스프링 핵심 원리 - 기본편 (2)](https://kkkkang1009.tistory.com/46)

[\[Spring\] 스프링 핵심 원리 - 기본편 (3)](https://kkkkang1009.tistory.com/50)

[\[Spring\] 스프링 핵심 원리 - 기본편 (4)](https://kkkkang1009.tistory.com/52)

## **싱글톤 컨테이너**

### **웹 애플리케이션과 싱글톤**

스프링은 온라인 서비스 기술을 지원하기 위해 탄생

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbAbFYu%2Fbtr2hJ2C0Po%2FuhKAIrKGtpBIfwTUDN7eb1%2Fimg.png)

순수 DI 컨테이너는 AppConfig 요청마다 객체를 새로 생성한다.  -> 해당 객체를 1개만 생성 후 공유하도록 설계한다. (싱글톤 패턴)

```
import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SingletonTest {

    @Test
    @DisplayName("DI Container without Spring ")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        // 조회마다 객체 생성
        MemberService memberService1 = appConfig.memberService();
        MemberService memberService2 = appConfig.memberService();

        // 참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // memSvc1 !== memSvc2
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);

    }
}
```

### **싱글톤 패턴**

클래스의 인스턴스가 1개만 생성되는 것을 보장하는 디자인 패턴

private 생성자를 사용하여 외부에서 임의로 new 키워드 사용하지 못하도록 막아야한다.

싱글톤 패턴 문제점

-   싱글톤 패턴 구현 코드가 복잡하다.
-   의존관계상 구체 클래스에 의존하여 DIP 위반한다.
-   클라이언트가 구체 클래스에 의존하여 OCP 위반 가능성이 높다.
-   테스트하기 어렵다.
-   내부 속성 변경 및 초기화 어렵다
-   private 생성자로 자식 클래스 만들기 어렵다
-   유연성이 떨어진다.
-   안티패턴으로 불리기도 한다.

```
public class SingletonService {
    // 1. static 영역에 객체 instance를 미리 하나 생성해둔다.
    private static final SingletonService instance = new SingletonService();

    // 2. 이 객체 인스턴스가 필요하면 'getInstance()' 메소드를 통해서만 조회할 수 있다.
    public static SingletonService getInstance(){
        return instance;
    }

    // 3. 딱 1개의 객체 인스턴스만 존재해야 하므로, 생성자를 private로 막아 외부에서 new 키워드로 생성되는 것을 막는다.
    private SingletonService(){
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
```

### **싱글톤 컨테이너**

스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하며, 객체 인스턴스를 관리한다.

스프링 컨테이너는 싱글톤 컨테이너 역할을 한다.

싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.

스프링의 기본 빈 등록 방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아니다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FCHtoq%2Fbtr1XPQzwLo%2FR5B3gcWG9vLUUsxk3xLZEK%2Fimg.png)

```
    @Test
    @DisplayName("Spring Container, Singleton")
    void springContainer() {
		// AppConfig appConfig = new AppConfig();
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        // 조회시 스프링 컨테이너에서 조회
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        // 참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // memSvc1 == memSvc2
        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
```

### **싱글톤 방식의 주의점** 

싱글톤 방식은 여러 클라이언트가 하나의 인스턴스를 공유하기 때문에 상태를 유지(stateful)하게 설계하면 안된다.

특정 클라이언트에 의존적인 필드가 있거나, 값을 변경할 수 있는 필드가 있으면 안된다.

지역변수, 파라미터, ThreadLocal 등 을 사용해야한다.

스프링 빈은 항상 무상태(stateless)로 설계한다.

StatefulService.java, StatefulServiceTest.java

```
public class StatefulService {

    private int price;

    public void order(String name, int price){
        System.out.println("name = " + name + " price = " + price);
        this.price = price;
    }

    public int getPrice(){
        return price;
    }
}
```

```
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void StatefulServiceSingleton(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : A가 10000원 주문
        statefulService1.order("userA", 10000);
        // ThreadA : B가 20000원 주문
        statefulService2.order("userB", 20000);

        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }

}
```

### **@Configuration과 싱글톤**

```
    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }
    
    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
    
    @Bean
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
```

MemberSerivce, OrderService에서 각각 memberRepository()를 호출하여 싱글톤이 깨지는 것 처럼 보인다.

```
예상
// call AppConfig.memberService
// call AppConfig.memberRepository
// call AppConfig.memberRepository
// call AppConfig.orderService
// call AppConfig.memberRepository

실제
// call AppConfig.memberService
// call AppConfig.memberRepository
// call AppConfig.orderService
```

실제로는 싱글톤이 깨지지 않는 것을 볼 수 있다.

```
import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository1 = " + memberRepository1);
        System.out.println("orderService -> memberRepository2 = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        // 모두 같은 인스턴스를 참고하고 있다.
        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }
}
```

### **@Configuration과 바이트코드 조작의 마법**

스프링은 CGLIB라는 바이트코드 조작 라이브러리를 사용한다.

AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고 그 다른 클래스를 스프링 빈으로 등록하여 사용한다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FuAHL5%2Fbtr1Wzgqnj5%2FjCS0iUj8aG0dqJJLmslCIK%2Fimg.png)

@Bean이 붙은 경우 빈이 이미 존재하면 해당 빈을 리턴하고, 없다면 생성하여 스프링 빈 등록 후 리턴하는 코드가 동적으로 만들어진다.

@Configuration 없이 @Bean만 적용하면 스프링 빈은 등록되지만, 싱글톤은 보장되지 않는다.

각각 다른 memberRepository가 생성된다.

```
// call AppConfig.memberService
// call AppConfig.memberRepository
// call AppConfig.memberRepository
// call AppConfig.orderService
// call AppConfig.memberRepository
```