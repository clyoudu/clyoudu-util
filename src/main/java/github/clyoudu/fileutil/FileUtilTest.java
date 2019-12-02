package github.clyoudu.fileutil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author leichen
 * @date 2019/12/2 5:11 下午
 */
@Slf4j
public class FileUtilTest {

    public static void main(String[] args) {
        log.info("{}", "test".equals(FileUtil.readClasspathFile("/test.txt")));
        log.info("{}", "test".equals(FileUtil.readOsFile( System.getProperty("user.dir") + "/src/main/resources/test.txt")));
    }

}
