package github.clyoudu.oom;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * java.lang.OutOfMemoryError: Requested array size exceeds VM limit
 * @author leichen
 * @date 2019/12/6 5:47 下午
 */
@Slf4j
public class RequestedArraySizeExceedsVmLimit {

    public static void main(String[] args) {
        List list = new ArrayList(Integer.MAX_VALUE);
    }

}
