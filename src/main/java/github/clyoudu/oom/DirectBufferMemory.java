package github.clyoudu.oom;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * java.lang.OutOfMemoryError: Direct buffer memory
 * VM Options: -Xmx64M -XX:MaxDirectMemorySize=20M
 * @author leichen
 * @date 2019/12/6 11:51 上午
 */
@Slf4j
public class DirectBufferMemory {
    public static void main(String[] args) {
        OOMUtil.checkMaxHeapSize();
        int size = Integer.MAX_VALUE;
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        log.info("{}", buffer.capacity());
    }
}
