package github.clyoudu.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * java.lang.OutOfMemoryError: GC overhead limit exceeded
 * VM Options: -Xmx64M
 * @author leichen
 * @date 2019/12/6 10:13 上午
 */
public class GCOverheadLimitExceeded {

    public static void main(String[] args) {
        OOMUtil.checkMaxHeapSize();
        List list = new ArrayList<>();
        int i = 1;
        while (i ++ > 0) {
            list.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + i);
        }
    }

}
