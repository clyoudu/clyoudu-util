package github.clyoudu.oom;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * java.lang.OutOfMemoryError: Java heap space
 * VM Options: -Xmx32M
 * @author leichen
 * @date 2019/12/6 9:48 上午
 */
@Slf4j
public class JavaHeapSpace {
    public static void main(String[] args) {
        OOMUtil.checkMaxHeapSize();
        List list = new ArrayList<>();
        while (true) {
            list.add(new Object());
        }
    }

}
