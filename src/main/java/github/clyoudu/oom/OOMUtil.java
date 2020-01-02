package github.clyoudu.oom;

import github.clyoudu.byteformat.ByteUnitFormat;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * @author leichen
 * @date 2019/12/6 10:19 上午
 */
@Slf4j
public class OOMUtil {

    private static final Integer MAX_HEAP_SIZE = 64 * 1024 * 1024;

    private OOMUtil () {}

    public static void checkMaxHeapSize () {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        log.info("Max heap size: " + ByteUnitFormat.B.to(ByteUnitFormat.M, memoryMXBean.getHeapMemoryUsage().getMax()));
        if (memoryMXBean.getHeapMemoryUsage().getMax() > MAX_HEAP_SIZE) {
            throw new IllegalArgumentException("Max heap size is 64M for this test.");
        }
    }

    public static void checkMaxPermSize () {
        if (Double.parseDouble(getJvmVersion().substring(0, 3)) >= 1.8) {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            log.info("Jvm options: " + runtimeMXBean.getInputArguments());
            if (runtimeMXBean.getInputArguments().stream().noneMatch(option -> option.startsWith("-XX:MaxMetaspaceSize="))) {
                throw new IllegalArgumentException("-XX:MaxPermSize must be set for this test.");
            }
        } else {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            log.info("Jvm options: " + runtimeMXBean.getInputArguments());
            if (runtimeMXBean.getInputArguments().stream().noneMatch(option -> option.startsWith("-XX:MaxPermSize="))) {
                throw new IllegalArgumentException("-XX:MaxPermSize must be set for this test.");
            }
        }
    }

    public static String getJvmVersion () {
        return System.getProperty("java.version");
    }

    public static void main(String[] args) {
        System.out.println(getJvmVersion());
    }

}
