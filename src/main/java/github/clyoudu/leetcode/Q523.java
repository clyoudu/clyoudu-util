package github.clyoudu.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leichen
 */
public class Q523 {

    public static void main(String[] args) {
        System.out.println(checkSubarraySum(new int[]{23, 2, 4, 6, 7}, 6));
        System.out.println(checkSubarraySum(new int[]{23, 2, 6, 4, 7}, 6));
        System.out.println(checkSubarraySum(new int[]{23, 2, 6, 4, 7}, 13));
        System.out.println(checkSubarraySum(new int[]{23, 2, 6, 4, 7}, 0));
        System.out.println(checkSubarraySum(new int[]{23}, 0));
        System.out.println(checkSubarraySum(new int[]{23, 3}, 0));
        System.out.println(checkSubarraySum(new int[]{0, 0}, 0));
        System.out.println(checkSubarraySum(new int[]{0}, 0));
    }

    private static boolean checkSubarraySum(int[] nums, int k) {
        int sum = 0;
        Map<Integer, Integer> sumModIndex = new HashMap<>(8);
        sumModIndex.put(0, -1);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];

            if (k != 0) {
                sum = sum % k;
            }
            if (sumModIndex.containsKey(sum)) {
                if (i - sumModIndex.get(sum) > 1) {
                    return true;
                }
            } else {
                sumModIndex.put(sum, i);
            }
        }
        return false;
    }

}
