package github.clyoudu.randompassword;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author leichen
 * @date 2019/11/21 4:33 下午
 */
public class RandomPasswordUtil {

    private static final List<String> UPPER = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    private static final List<String> LOWER = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
    private static final List<String> SPECIAL = Arrays.asList("~", "!", "@", "#", "$", "%", "^", "&", "*");
    private static final List<String> NUMBER = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private RandomPasswordUtil() {}

    public static synchronized String randomPassword(Integer length, Integer lower, Integer upper, Integer special, Integer number) {
        Collections.shuffle(UPPER);
        Collections.shuffle(LOWER);
        Collections.shuffle(SPECIAL);
        Collections.shuffle(NUMBER);

        List<String> all = new ArrayList<>();
        addAll(lower, LOWER, all);
        addAll(upper, UPPER, all);
        addAll(number, NUMBER, all);
        addAll(special, SPECIAL, all);

        Collections.shuffle(all);
        if (lower + upper + special + number > length) {
            throw new IllegalArgumentException("Sum(lower, upper, special, number) > length");
        }

        List<String> result = new ArrayList<>();
        fillPassword(result, lower, LOWER);
        fillPassword(result, lower + upper, UPPER);
        fillPassword(result, lower + upper + special, SPECIAL);
        fillPassword(result, number + lower + upper + special, NUMBER);
        fillPassword(result, length, all);

        Collections.shuffle(result);

        return StringUtils.join(result, "");
    }

    private static void addAll(Integer count, List<String> list, List<String> all) {
        if (count > 0) {
            all.addAll(list);
        }
    }

    public static String defaultModelRandomPassword() {
        return randomPassword(16, 1, 1, 1, 1);
    }

    private static void fillPassword(List<String> result, Integer count, List<String> charList) {
        int index = 0;
        while (result.size() < count) {
            result.add(charList.get(index));
            if (index < charList.size() - 1) {
                index ++;
            } else {
                index = 0;
            }
        }
    }
}
