package github.clyoudu.oom;

import lombok.extern.slf4j.Slf4j;

/**
 * java.lang.OutOfMemoryError: unable tocreate new native thread
 * Note: May cause the operating system to be unavailable
 * @author leichen
 * @date 2019/12/6 5:37 下午
 */
@Slf4j
public class UnableToCreateNewNativeThread {

    public static void main(String[] args) {
        Runnable t = () -> {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                log.error("", e);
                Thread.currentThread().interrupt();
            }
        };

        for(int i = 0 ; i < 999999999;i++){
            new Thread(t).start();
        }
    }

}
