package github.clyoudu.leetcode;

/**
 * @author leichen
 */
public class JianZhiOffer1_58 {

    public static void main(String[] args) {
        System.out.println(leftLift("abcdefg", 2));
        System.out.println(leftLift("lrloseumgh", 6));
    }

    private static String leftLift(String s, int length) {
        if (length <= 0 || length >= s.length()) {
            return s;
        }

        return s.substring(length) + s.substring(0, length);
    }

}
