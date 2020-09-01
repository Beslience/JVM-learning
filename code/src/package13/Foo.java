package package13;

/**
 * @author zhengjiayuan
 * @Create 2020-08-31
 */
public class Foo {

//    public static void foo() throws Exception {
//        try (AutoCloseable c = new AutoCloseable() {
//            @Override
//            public void close() throws Exception {
//
//            }
//        }) {
//            bar();
//        }
//    }

    public static void foo() throws Exception {
        AutoCloseable c = null;
        Exception tmpException = null;
        try {
            c = new AutoCloseable() {
                @Override
                public void close() throws Exception {

                }
            };
            bar();
        } catch (Exception e) {
            tmpException = e;
            throw e;
        } finally {
            if (c  != null) {
                if (tmpException != null) {
                    try {
                        c.close();
                    } catch (Exception e) {
                        tmpException.addSuppressed(e);
                    }
                } else {
                    c.close();
                }
            }
        }
    }

    public static void bar() {
        // may throw exception
    }

}
