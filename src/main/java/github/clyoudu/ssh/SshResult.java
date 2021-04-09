/*
 * <p>文件名称: SshResult.java</p>
 * <p>项目描述: KOCA 金证云原生平台 v2.1.0</p>
 * <p>公司名称: 深圳市金证科技股份有限公司</p>
 * <p>版权所有: (C) 2019-2020</p>
 */

package github.clyoudu.ssh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ssh执行结果.
 *
 * @author leichen
 * @since 2.1.0, 2021/2/9 1:56 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SshResult {

    /**
     * linux exit code
     */
    private Integer exitStatus;

    /**
     * 控制台输出
     */
    private String stdOut;

    /**
     * 控制台错误
     */
    private String stdErr;

}
