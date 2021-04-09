package github.clyoudu.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

/**
 * @author leichen
 */
public class Q1 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));
        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));
    }

    private static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numIndexMap = new HashMap<>(8);
        for (int i = 0; i < nums.length; i++) {
            numIndexMap.put(nums[i], i);
        }

        int[] result = new int[2];

        for (int i = 0; i < nums.length; i++) {

            Integer index = numIndexMap.get(target - nums[i]);
            if (index != null && index != i) {
                result[0] = i;
                result[1] = index;
                return result;
            }
        }

        return result;
    }

}
