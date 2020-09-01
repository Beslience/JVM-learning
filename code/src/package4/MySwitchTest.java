package package4;

/**
 * @author zhengjiayuan
 * @Create 2020-08-31
 */
public class MySwitchTest {

    int choooseNear(int i) {
        switch (i) {
            case 100: return 0;
            case 101: return 1;
            case 104: return 4;
            default: return -1;
        }
    }

    int chooseFar(int i ) {
        switch (i) {
            case 1: return 1;
            case 10: return 10;
            case 100: return 100;
            default: return -1;
        }
    }

}
