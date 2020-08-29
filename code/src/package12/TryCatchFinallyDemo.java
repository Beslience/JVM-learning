package package12;

/**
 * @author zhengjiayuan
 * @Create 2020-08-01
 */
public class TryCatchFinallyDemo {

    public static int func1() {
        try {
            return 0;
        } catch (Exception e){
            return 1;
        } finally {
            return 2;
        }
    }

    public static int func2() {
        try {
            int a = 1 / 0;
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            return 2;
        }
    }

    public static int func3() {
        try {
            int a = 1 / 0;
            return 0;
        } catch (Exception e) {
            int b = 1 / 0;
        } finally {
            return 2;
        }
    }

    public static void main(String[] args) {
        System.out.println(func1());
        System.out.println(func2());
        System.out.println(func3());

    }

}
