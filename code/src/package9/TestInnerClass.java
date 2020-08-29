package package9;

/**
 * 测试匿名内部类的实现
 *
 * 生成 TestInnerClass$1.class
 *
 * @author zhengjiayuan
 * @Create 2020-08-27
 */
public class TestInnerClass {

    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello, inner class");
            }
        };
        r1.run();
    }

}
