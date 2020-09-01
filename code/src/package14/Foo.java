package package14;

/**
 * @author zhengjiayuan
 * @Create 2020-08-31
 */
public class Foo {

    /**
     *        0: getstatic     #2                  // Field lock:Ljava/lang/Object;
     *        3: dup
     *        4: astore_1
     *        5: monitorenter
     *        6: invokestatic  #3                  // Method bar:()V
     *        9: aload_1
     *       10: monitorexit
     *       11: goto          19
     *       14: astore_2
     *       15: aload_1
     *       16: monitorexit
     *       17: aload_2
     *       18: athrow
     *       19: return
     *     Exception table:
     *        from    to  target type
     *            6    11    14   any
     *           14    17    14   any
     */
    private static Object lock = new Object();

    public static void main(String[] args) {
        synchronized (lock) {
            bar();
        }
    }

    public static void bar() {

    }

}
