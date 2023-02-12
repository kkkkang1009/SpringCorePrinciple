이전 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (1)](https://kkkkang1009.tistory.com/44)

## **스프링의 핵심 원리 이해1 - 예제 만들기**

### **1.  프로젝트 생성**

-   [https://start.spring.io/](https://start.spring.io/) 에서 설정 후 spring boot 프로젝트 생성한다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fdnibvw%2FbtrW38fwaLs%2FDSV2ZXTwPyYUU4PlLKZGEK%2Fimg.png)

-   만들어진 프로젝트를 압축해제 후 Intellij에서 Import하여 확인한다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FDJ2Rt%2FbtrWSvX2EIz%2FBKAyXxYLdOrje2EzmdSkM0%2Fimg.png)

-   src/main/java/{group}/{Artifact}/{Artifate}Application을 run하여 정상 작동 여부를 확인한다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Ftvtg1%2FbtrWU792Yzy%2FkBxSomg9gnyupI4Dk4vpK0%2Fimg.png)
![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmArG4%2FbtrWX4Sxp5S%2FUv7kNfxPMkWAAmKo0EDoKk%2Fimg.png)

-   Intellij gradle 세팅 변경

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbjdOr7%2FbtrW1On4qFF%2Fo0nToJyvvaTVLJKmIUQWzK%2Fimg.png)

### **2\. 비즈니스 요구사항과 설계**

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fb9poe8%2FbtrWUdWX55s%2FbUKrNpGDX8z2v2bDn0NNuK%2Fimg.png)

### **3\. 회원 도메인 설계**

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FQnNIB%2FbtrW39yJWPL%2FJ0qLycMku6LLmLqmEqWy61%2Fimg.png)

### **4\. 회원 도메인 개발**

-   Grade, Member  - model

```
public enum Grade {
    BASIC,
    VIP
}
```

```
public class Member {

    private Long id;
    private String name;
    private Grade grade;

    public Member(Long id, String name, Grade grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
```

-   MemberRepository, MemberService - Interface

```
public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);
}
```

```
public interface MemberService {
    void join(Member member);

    Member findMember(Long memberId);
}
```

-   MemoryMemberRepository, MemberServiceImpl - implements

```
import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
```

```
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
```

### **5\. 회원 도메인 실행과 테스트**

-   MemberApp 을 통해 정상 동작 확인 및 JUnit 테스트 코드 추가

```
package hello.core;

import hello.core.member.*;

public class MemberApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("findMember = "+findMember.getName());
        System.out.println("member = " + member.getName());
    }
}
```

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbKrjG6%2FbtrWXi4xdKq%2FK9gkZnCM9nskqSDVsck6Nk%2Fimg.png)

```
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {
    MemberService memberService = new MemberServiceImpl();

    @Test
    void join(){
        //given
        Member member = new Member(1L, "memberA", Grade.VIP);

        //when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
```

### **6\. 주문과 할인 도메인 설계**

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Ftww0T%2FbtrW4FLcK3K%2FgmUWvW5i5kPwrxECS9wcl1%2Fimg.png)

### **7.  주문과 할인 도메인 개발**

-   Order - model

```
public class Order {
    private Long memberId;
    private String itemName;
    private int itemPrice;
    private int discountPrice;

    public Order(Long memberId, String itemName, int itemPrice, int discountPrice) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.discountPrice = discountPrice;
    }

    public int calculatePrice() {
        return itemPrice - discountPrice;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "memberId=" + memberId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", discountPrice=" + discountPrice +
                '}';
    }
}
```

-   DiscountPolicy, OrderService - interface

```
import hello.core.member.Member;

public interface DiscountPolicy {
    /**
     * @return 할인 대상 금액
     */
    int discount(Member member, int price);
}
```

```
public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
```

-   FixDiscountPolicy, OrderServiceImpl - implement

```
import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
```

```
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository= new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy= new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
```

### **8\. 주문과 할인 도메인 실행과 테스트**

-   OrderApp 을 통해 정상 동작 확인 및 JUnit 테스트 코드 추가

```
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);
    }
}
```

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOxw2W%2FbtrWTKHxE3U%2FY5hK82SpEzRxLRpEPilMMk%2Fimg.png)

```
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {
    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    void createOrder() {
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
```

다음 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (3)](https://kkkkang1009.tistory.com/50)

[\[Spring\] 스프링 핵심 원리 - 기본편 (4)](https://kkkkang1009.tistory.com/52)