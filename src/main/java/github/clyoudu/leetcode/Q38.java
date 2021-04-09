package github.clyoudu.leetcode;

/**
 * @author leichen
 */
public class Q38 {

    public static void main(String[] args) {
        System.out.println(countAndSay(1));
        System.out.println(countAndSay(2));
        System.out.println(countAndSay(3));
        System.out.println(countAndSay(4));
        System.out.println(countAndSay(5));
        System.out.println(countAndSay(6));
        System.out.println(countAndSay(7));
        System.out.println(countAndSay(8));
        System.out.println(countAndSay(9));
        System.out.println(countAndSay(10));
    }

    private static String countAndSay(int n) {
        String result = "";

        for (int i = 1; i <= n; i++) {
            if (i == 1) {
                result = "1";
            } else {
                result = countAndSay(result);
            }
        }

        return result;
    }

    private static String countAndSay(String result) {
        StringBuilder builder = new StringBuilder();
        char c = result.charAt(0);
        int count = 1;
        for (int i = 1; i < result.length(); i++) {
            if (c == result.charAt(i)) {
                count ++;
            } else {
                builder.append(count).append(c);
                c = result.charAt(i);
                count = 1;
            }
        }

        builder.append(count).append(c);
        return builder.toString();
    }

}
