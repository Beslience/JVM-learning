package package4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengjiayuan
 * @Create 2020-08-31
 */
public class MyLoopTest {

    public static int[] numbers = new int[]{1, 2, 3};

    public static void main(String[] args) {
        List<Integer> scores = new ArrayList<>();
        for (int number : numbers) {
            scores.add(number);
        }
    }
}

/**
 *
 * @start: if ($i >= $len) return;
 * $item = $array[$i]
 * ++ $i
 * goto @start
 *
 **/