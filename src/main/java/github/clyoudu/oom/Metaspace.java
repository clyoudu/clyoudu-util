package github.clyoudu.oom;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * jdk7-:
 *      java.lang.OutOfMemoryError: PermGen space
 *      VM Options: -XX:PermSize=10M -XX:MaxPermSize=10M
 * jdk8+:
 *      java.lang.OutOfMemoryError: Metaspace
 *      VM Options: -XX:MetaspaceSize=10M -XX:MaxMetaspaceSize=10M
 * @author leichen
 * @date 2019/12/6 11:11 上午
 */
public class Metaspace {
    public static void main(String[] args) {
        OOMUtil.checkMaxPermSize();
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Metaspace.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> proxy.invokeSuper(obj, args1));
            enhancer.create();
        }
    }
}
