package package9;

/**
 * 测试 lambda 表达式
 *
 *
 * @author zhengjiayuan
 * @Create 2020-08-27
 */
public class TestLambda {

    public static void main(String[] args) {
        Runnable r = ()->{
            System.out.println("hello, lambda");
        };
        r.run();
        System.out.println(r.getClass().getName());
    }

}
