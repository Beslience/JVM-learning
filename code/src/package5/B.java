package package5;

/**
 * @author zhengjiayuan
 * @Create 2020-08-31
 */
public class B extends A {

    static {
        System.out.println("B init");
    }

    public B() {
        System.out.println("B Instance");
    }

    public static void main(String[] args) {
        A a = new B();
    }
}
