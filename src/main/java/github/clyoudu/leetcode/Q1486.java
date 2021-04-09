package github.clyoudu.leetcode;

/**
 * @author leichen
 */
public class Q1486 {

    public static void main(String[] args) {
        System.out.println(xorOperation(5, 0));
        System.out.println(xorOperation(4, 3));
        System.out.println(xorOperation(1, 7));
    }

    private static int xorOperation(int n, int start) {
        int result = start;

        int index = 1;
        while (index < n) {
            result = result ^ (start + 2 * index);
            index ++;
        }

        return result;
    }

}
