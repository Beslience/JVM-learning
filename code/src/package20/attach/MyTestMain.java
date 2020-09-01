package package20.attach;

import java.util.concurrent.TimeUnit;

/**
 * @author zhengjiayuan
 * @Create 2020-09-01
 */
public class MyTestMain {

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            System.out.println(foo());
            TimeUnit.SECONDS.sleep(3);
        }
    }

    public static int foo() {
        // 修改 return 50;
        return 100;
    }
}
