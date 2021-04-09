package github.clyoudu.leetcode;

import java.util.Arrays;

/**
 * @author leichen
 */
public class Q1480 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(runningSum(new int[]{1,2,3,4})));
        System.out.println(Arrays.toString(runningSum(new int[]{1,1,1,1,1})));
        System.out.println(Arrays.toString(runningSum(new int[]{3,1,2,10,1})));
    }

    private static int[] runningSum(int[] nums) {
        int[] result = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            if(i == 0) {
                result[i] = nums[i];
            } else {
                result[i] = result[i - 1] + nums[i];
            }
        }
        return result;
    }

    /*private static int runningSumIndex(int[] nums, int index) {
        if(index == 0) {
            return nums[0];
        } else {
            return nums[index] + runningSumIndex(nums, index - 1);
        }
    }*/

}
