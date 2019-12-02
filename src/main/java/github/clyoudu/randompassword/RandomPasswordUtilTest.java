package github.clyoudu.randompassword;

import lombok.extern.slf4j.Slf4j;

/**
 * @author leichen
 * @date 2019/12/2 5:57 下午
 */
@Slf4j
public class RandomPasswordUtilTest {

    public static void main(String[] args) {
        log.info("{}", RandomPasswordUtil.randomPassword(16, 2, 2, 2, 2));
        log.info("{}", RandomPasswordUtil.randomPassword(16, 2, 2, 2, 2));
        log.info("{}", RandomPasswordUtil.defaultModelRandomPassword());
        log.info("{}", RandomPasswordUtil.defaultModelRandomPassword());
        log.info("{}", RandomPasswordUtil.randomPassword(8, 1, 1, 1, 1));
        log.info("{}", RandomPasswordUtil.randomPassword(8, 1, 1, 1, 1));
        log.info("{}", RandomPasswordUtil.randomPassword(8, 1, 1, 0, 1));
        log.info("{}", RandomPasswordUtil.randomPassword(8, 1, 1, 0, 1));
    }

}
