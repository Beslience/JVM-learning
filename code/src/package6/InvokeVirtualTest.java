package package6;

/**
 * @author zhengjiayuan
 * @Create 2020-08-31
 */
public class InvokeVirtualTest {

    private static Color yellowColor = new Yellow();
    private static Color redColor = new Red();

    public static void main(String[] args) {
        /**
         *        0: getstatic     #2                  // Field yellowColor:Lpackage6/Color;
         *        3: invokevirtual #3                  // Method package6/Color.printColorName:()V
         *        6: getstatic     #4                  // Field redColor:Lpackage6/Color;
         *        9: invokevirtual #3                  // Method package6/Color.printColorName:()V
         *       12: return
         */
        yellowColor.printColorName();
        redColor.printColorName();
    }
}
