package package20.attach;

import com.sun.tools.attach.VirtualMachine;

/**
 * @author zhengjiayuan
 * @Create 2020-09-01
 */
public class MyAttachMain {

    public static void main(String[] args) throws Exception {
        VirtualMachine vm = VirtualMachine.attach(args[0]);
        try {
            vm.loadAgent("/Users/cvter/Desktop/JVM-learning/code/my-attach/target/my-attach-agent.jar");
            System.in.read();
        } finally {
            vm.detach();
        }
    }
}
