package github.clyoudu.leetcode;

import java.util.Arrays;

/**
 * @author leichen
 */
public class Q1470 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(shuffle(new int[]{2,5,1,3,4,7}, 3)));
        System.out.println(Arrays.toString(shuffle(new int[]{1,2,3,4,4,3,2,1}, 4)));
        System.out.println(Arrays.toString(shuffle(new int[]{1,1,2,2}, 2)));
    }

    private static int[] shuffle(int[] nums, int n) {
        int[] result = new int[2 * n];
        for (int i = 0, j = n, index = 0; i < n && j < 2 * n; i++, j++, index += 2) {
            result[index] = nums[i];
            result[index + 1] = nums[j];
        }
        return result;
    }

}
