package hello.core.singleton;

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