package package10;

/**
 * @author zhengjiayuan
 * @Create 2020-08-01
 */
public class Foo {

    public static void foo1() {
        int i = 0;
        for (int j = 0; j < 50; j++) {
            i = i++;
        }
        System.out.println(i);
    }

    public static void foo2() {
        int i = 0;
        for (int j = 0; j < 50; j++) {
            i = ++i;
        }
        System.out.println(i);
    }

    public static void bar1() {
        int i = 0;
        i = i++ + ++i;
        System.out.println("i = " + i);
    }

    public static void bar2() {
        int i = 0;
        i = ++i + i++;
        System.out.println("i = " + i);
    }

    public static void bar3() {
        int i = 0;
        i = ++i + i++ + i++ + i++;
        System.out.println(i);
    }

    public static void main(String[] args) {
        foo1();
        foo2();
        bar1();
        bar2();
        bar3();
    }

}
