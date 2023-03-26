이전 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (6)](https://kkkkang1009.tistory.com/54)

## **의존관계 자동 주입**

### **다양한 의존관계 주입 방법**

생성자 주입

- 생성자 호출시점에 1회 호출 보장

- 불변, 필수 의존관계에 사용

- @Autowired(생성자가 딱 하나만 있으면 @Autowired 생략해도 자동 주입 - spring bean인 경우)

수정자 주입

- setter에 Autowired 적용

- 선택(required=false), 변경 의존관계에 사용

필드 주입

- @Autowired private MemberRepository memberRepository;

- 외부에서 변경 불가능 ->  setter 만들어야함

-  안쓰는게 좋으나 @Configuration 혹은 테스트 과정 중에는 가능

일반 메서드 주입

- 일반 메서드를 통해 주입

- 한번에 여러 필드를 주입 가능

- 스프링 빈이 아닌 클래스에서는 @Autowired 적용해도 동작 X

### **옵션 처리**

주입할 스프링 빈이 없어도 동작해야할 때가 있다.

@Autowired(required=false) : 자동 주입 대상이 없으면 메서드 자체가 호출 X

org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력

Optional<> : 자동 주입할 대상이 없으면 Optional.empty가 입력

```
import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {
        
        @Autowired(required = false)
        public void setNoBean1(Member noBean1){
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired
        public void setNoBean2(@Nullable Member noBean2){
            System.out.println("noBean2 = " + noBean2);
        }

        @Autowired
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println("noBean3 = " + noBean3);
        }

    }
}
```

Member는 Spring bean이 아니므로

1번은 호출 X, 2번은 null, 3번은 Optional.empty 가 된다.

### **생성자 주입을 선택해라!**

과거 수정자, 필드 주입 방식을 많이 사용, 최근엔 DI 프레임워크 대부분 생성자 주입을 권장

**불변** - 대부분의 의존관계 주입은 한번 호출하면 애플리케이션 종료시점까지 의존관계 변경 안한다.

**누락** - 누락시 Null Point Exception 발생하여 컴파일 오류가 발생하여 막아준다.

**final 키워드** - 생성자에서 혹시 값이 설정되지 않는 오류를 컴파일 시점에서 막아준다.

-   생성자 주입 방식은 프레임워크에 의존하지 않고, 순수 자바 언어의 특징을 잘 살리는 방법
-   기본적으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여한다.
-   필드 주입은 사용하지 않는 것이 좋다.

### **롬복과 최신 트랜드**

@RequiredArgsConstructor 기능을 사용하여 final이 붙은 필드를 모아 생성자를 자동으로 만들어줌

롬복이 자바의 어노테이션 프로세서라는 기능을 통해 컴파일 시점에 생성자 코드를 자동으로 생성해준다.

### **조회 빈이 2개 이상 - 문제**

클래스 타입으로 조회하기 때문에 조회 빈이 2개 이상이면 에러가 발생한다.

```
ac.getBean(DiscountPolicy.class)
```

FixDiscountPolicy, RateDiscountPolicy 둘다 스프링 빈으로 조회되어 NoUniqueBeanDefinitionException 발생한다.

### **@Autowired 필드명, @Qualifier, @Primary**

@Autowired 필드명 매칭

1. 타입 매칭

2. 타입 매칭 결과 2개 이상일 경우 필드 명, 파라미터 명으로 빈 이름 맻이

```
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy rateDiscountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = rateDiscountPolicy;
}
```

@Qualifier (추가 구분자 느낌)

1. @Qualifier끼리 매칭

2. 못찾으면 mainDiscountPolicy라는 이름의 스프링 빈을 추가로 찾는다

3. NoSuchBeanDefinitionException 예외 발생

```
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy{
	''' '''
}
```

```
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

@Primary (우선 순위 지정)

1. 타입 매칭

2. 타입 매칭 결과 2개 이상일 경우 @Primary가 있는 빈  반환

**@Qualifier가 @Primary보다 우선순위가 높다.**

EX) 메인 DB 커넥션 관리 스프링 빈과 서브 DB 커넥션 관리 스프링 빈의 경우

- 메인 스프링 빈은 @Primary 적용하고, 서브 스프링 빈을 사용할 때는 @Qualifier를 지정해서 명시적으로 획득하는 방식을 사용

### **어노테이션 직접 만들기**

@Qualifier는 문자라 컴파일시 타입 체크가 안되는데 어노테이션을 만들어 문제를 해결할 수 있다.

```
import org.springframework.beans.factory.annotation.Qualifier;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```

어노테이션 직접 설정

```
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{
	''' '''
}
```

생성한 어노테이션을 클래스에 적용

```
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

등록한 어노테이션 클래스 사용 코드

### **조회한 빈이 모두 필요할 때,  List, Map**

해당 타입의 스프링 빈이 다 필요한 경우도 있다.

Ex) 클라이언트가 할인의 종류를 선택 가능한 경우

```
static class DiscountService {
    private final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policyList;

    @Autowired
    public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList){
        this.policyMap = policyMap;
        this.policyList = policyList;
        System.out.println("policyMap = " + policyMap);
        System.out.println("policyList = " + policyList);
    }

    public int discount(Member member, int price, String discountCode){
        DiscountPolicy discountPolicy = policyMap.get(discountCode);
        return discountPolicy.discount(member, price);
    }
}
```

DiscountPolicy 다형성을 Map, List에 스프링 빈으로 주입받아 사용할 수 있다.

### **자동, 수동의 올바른 실무 운영 기준**

@Component 만으로 끝날 수 있는 일을 @Configuration, @Bean 적용 및 객체 생성, 주입 대상 설정 번거롭다.

@Component로 자동 빈 등록을 사용해도 OCP, DIP를 지킬 수 있다.

업무 로직 빈 : 웹 지원 컨트롤러, 핵심 비즈니스 로직 서비스, 데이터 계층 로직 처리 레포지토리 등.

- Controller, Service, Repository처럼 유사한 패턴이 있는 경우 자동 기능을 사용하는 것이 좋다. 자동시 문제 발생시 파악이 쉽다.

- 다형성 필요시 수동 빈 등록을 사용하여 한눈에 파악할 수 있도록 하거나, 자동 빈 등록시 특정 패키지에 같이 묶어 두는 것이 좋다.

기술 지원 빈 : 기술적인 문제나 공통 관심사(AOP)를 처리할 떄 주로 사용한다. DB연결, 공통 로그 처리 등.

- 광범위하게 영향을 미치므로 수동 빈 등록을 사용하여 명확하게 들어내는 것이 좋다.

스프링, 스프링 부트가 자동 등록하는 빈들은 예외

내가 직접 기술 지원 객체를 스프링 빈에 등록한다면 수동 등록하여 명확하게 들어내는 것이 좋다.