이전 글

[\[Spring\] 스프링 핵심 원리 - 기본편 (5)](https://kkkkang1009.tistory.com/53)

## **컴포넌트 스캔**

### **컴포넌트 스캔과 의존관계 자동 주입 시작하기**

@ComponentScan을 붙여주면 컴포넌트 스캔 가능

@Configuration도 @Component가 붙기 때문에 자동 등록된다.

excludeFilters를 이용하여 Annotation 타입이 Configuration인 것들을 제거한다.

@Autowired를 사용하면 자동 매핑된다. -> default로 타입이 같은 빈을 주입한다.

@Component 동작 과정

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F0tmKO%2Fbtr2FhkJUWE%2F81sVlbUAbHQjQez6jqIjRK%2Fimg.png)

@Autowired 동작 과정

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbNwSPc%2Fbtr2D6cIKkS%2F2hc1Ma0rCA5hOwsMxUnjDk%2Fimg.png)

각 구현 클래스에 @Component, @Autowired annotation 추가 후 아래와 같은 Configuration 파일 작성시 동작 가능

```
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

}
```

### **탐색 위치와 기본 스캔 대상**

@ComponentScan에서 basePackage로 기본 스캔 패키지 설정 가능하며 여러 basePackage 등록 가능

```
@Configuration
@ComponentScan(
        basePackages = {"hello.core.member","hello.core.order"}
)
```

basePackageClasses로 상위 클래스 설정 가능

AppConfig 파일 위치를 기준으로 Component scan 동작 -> 프로젝트 시작 루트에 두고 하위 패키지 스캔

@Component, @Controller, @Service, @Repository, @Configuration 은 컴포넌트 기본 스캔 대상이다.

### **필터**

커스텀 Annotation을 이용하여 Include, Exclude를 할 수 있다.

```
    @Configuration
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class CoponentFilterAppConfig{

    }
```

FilterType 옵션

-   ANNOTATION : default, 어노테이션을 인식하여 동작
-   ASSIGNABLE\_TYPE : 지정한 타입과 자식 타입을 인식하여 동작
-   ASPECTJ : AspectJ 패턴 사용
-   REGEX : 정규 표현식
-   CUSTIOM : TypeFilter라는 인터페이스를 구현해서 처리

### **중복 등록과 충돌**

자동 빈 등록 vs 자동 빈 등록

ConflictingBeanDefinitionException 발생

수동 빈 등록 vs 자동 빈 등록

수동 등록 빈이 우선권을 가진다(Overriding)

최신 Spring boot 버전에서는 충돌시 오류 발생 -> overring true 설정시 동작 가능