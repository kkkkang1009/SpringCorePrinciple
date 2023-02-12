이전 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (1)](https://kkkkang1009.tistory.com/44)

[\[Spring\] 스프링 핵심 원리 - 기본편 (2)](https://kkkkang1009.tistory.com/46)

## **스프링의 핵심 원리 이해2 - 객체 지향 원리 적용**

### **1\. 새로운 할인 정책 개발**

추가 할인 정책 추가

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbt4KNL%2FbtrXm7oxc5P%2FJdn9fkTuW3sXdvsZqqi680%2Fimg.png)

RateDiscountPolicy, RateDiscountPolicyTest 파일 작성

```
import hello.core.member.Grade;
import hello.core.member.Member;

public class RateDiscountPolicy implements DiscountPolicy{

    private int discountPercent = 10;
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
```

```
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP 10% discount")
    void vip_o(){
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        int discount = discountPolicy.discount(member, 10000);

        Assertions.assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("Not VIP no discount")
    void vip_x(){
        Member member = new Member(1L, "memberBASIC", Grade.BASIC);

        int discount = discountPolicy.discount(member, 10000);

        Assertions.assertThat(discount).isEqualTo(0);
    }
}
```

### **2\. 새로운 할인 정책 적용과 문제점**

신규 정책을 적용하려면 기존의 OrderServiceImpl 코드 수정이 필요하다.

```
public class OrderServiceImpl implements OrderService{
	// ...
    
//    private final DiscountPolicy discountPolicy= new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy= new RateDiscountPolicy();
    
	// ...
}
```

**DIP 위반** : 인터페이스뿐만 아니라 구현 클래스도 함께 의존하고 있다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbc9RQW%2FbtrXrNIETtB%2F9a21RFq4tWwGRPZxFp9tbK%2Fimg.png)

**OCP 위반** : 할인 정책이 변경되면 OrderServiceImpl 코드 수정이 필요하다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fb42FF1%2FbtrXoCHSAx1%2FuD5DnE3ZSNiiIZLWMWuIG1%2Fimg.png)

DIP, OCP 위반을 하지 않으려면, 누군가 OrderServiceImpl의 DiscountPolicy 구현 객체를 대신 주입해줘야한다.

### **3\. 관심사의 분리**

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FIyu5T%2FbtrXnHJF4lx%2FZIOw017Ii7rLRQ3BApAvTk%2Fimg.png)

AppConfig에 MemberRepository, DiscountPolicy 구현체를 주입하는 코드 추가

AppConfigMemoryMemberRepository 객체를 생성 후 그 memberServiceImpl 생성하면서 생성자로 전달

```
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class AppConfig {
    public MemberService memberService(){
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService(){
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
```

MemberService, OrderService 선언부 코드 변경 필요

```
    AppConfig appConfig = new AppConfig();
    MemberService memberService = appConfig.memberService();
    OrderService orderService = appConfig.orderService();
```

### **4\. AppConfig 리팩터링**

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFmTO0%2FbtrXmJuGo0O%2FY1HKY7OE1QN1Zz7KfWd0Ck%2Fimg.png)

중복을 제거하고, 각 역할과 구현 클래스를 보기 좋게 리팩토링한다.

```
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class AppConfig {
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
}
```

### **5\. 새로운 구조와 할인 정책 적용**

AppConfig를 통해 사용 영역과 구성 영역으로 구조가 분리되었다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fzr6zA%2FbtrXrN2YRv6%2F6mWLrXkLuletRkWxpvzeM0%2Fimg.png)

### **6\. 전체 흐름 정리**

인터페이스뿐만 아니라 구체 클레스도 함께 의존 -> DIP 위반

AppConfig를 통해 애플리케이션의 전체 동작 방식을 구성 (구현 객체를 생성하고, 연결하는 역할)

사용영역과 구성영역이 분리됨으로써 구성 영역만 변경하면 할인 정책 변경 적용 가능

### **7\. 좋은 객체 지향 설계의 5가지 원칙의 적용**

SRP 단일 책임 원칙 : 한 클래스는 하나의 책임만 가져야한다.

-   구현 객체 생성 및 연결은 AppConfig가 담당
-   클라이언트 객체는 실행하는 역할만 담담

DIP 의존관계 역전 원칙 : 추상화에 의존하고, 구체화에 의존하면 안된다.

-   AppConfig가 구현 객체 인스턴스를 클라이언트 코드 대신 생성하여 의존관계를 주입

OCP : 소프트웨어 요소는 확장에는 열려 있으나, 변경에는 닫혀 있어야한다.

-   애플리케이션을 사용, 구성 영역으로 분리
-   AppConfig가 의존 관계를 주입해주므로 클라이언트 코드 변경 없음

### **8\. IoC, DI, 그리고 컨테이너**

제어 역전 IoC (Inversion of Control)

기존에는 구현 객체가 스스로 생성하고 호출하는 등 제어 흐름을 스스로 조종하였으나

AppConfig 등장 후 자신의 로직을 실행하는 역할만 담당한다.

프레임워크 VS 라이브러리

작성한 코드의 제어와 실행을 대신한다면 프레임워크

코드 자체가 제어 흐름을 담당한다면 라이브러리

의존관계 주입 DI (Dependency Injection)

의존관계는 **정적인 클래스 의존 관계와, 실행 시점에 결정되는 동적인 객체 의존 관계** 둘을 분리해서 생각해야한다.

정적인 클래스 의존 관계 : import 코드만 보고 의존 관계를 쉽게 판단 가능

동적인 객체 인스턴스 의존 관계 : 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존 관계

DI를 사용한다면 정적인 클래스 의존 관계 변경 없이, 동적인 객체 인스턴스 의존관계를 쉽게 변경 가능

IoC 컨테이너, DI 컨테이너

AppConfig처럼 객체를 생성하고 의존관계를 연결해주는 것

### **9\. 스프링으로 전환하기**

ApplicationContext를 스프링 컨테이너라고 한다.

@Configuration이 붙은 AppConfig를 구성 정보로 사용한다.

@Bean이 붙은 함수를 모두 호출하여 반환된 객체를 스프링 컨테이너에 등록한다. 등록된 객체를 스프링 빈이라고 한다.

스프링 빈은 함수 명을 이름으로 사용한다.(디폴트의 경우)

```
// ...
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }
    // ...
}
```

Spring Bean으로 변경 후 ApplicationContext를 통해 객체를 가져온다.

```
// ...
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService",MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService",OrderService.class);
        // ...
	}
}
```

다음 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (4)](https://kkkkang1009.tistory.com/52)