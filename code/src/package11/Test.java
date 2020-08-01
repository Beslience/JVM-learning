package package11;

/**
 * @author zhengjiayuan
 * @Create 2020-08-01
 */
public class Test {

    public int test(String name) {
        switch (name) {
            case "Java" :
                return 100;
            case "Kotlin" :
                return 200;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        Test t = new Test();
        System.out.println(t.test("Kotlin"));
    }
}
