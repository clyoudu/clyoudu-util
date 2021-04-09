package github.clyoudu.leetcode;

/**
 * @author leichen
 */
public class Q665 {

    public static void main(String[] args) {
        System.out.println(checkPossibility(new int[]{4, 2, 3}));
        System.out.println(checkPossibility(new int[]{4, 2, 1}));
        System.out.println(checkPossibility(new int[]{3, 4, 2, 3}));
        System.out.println(checkPossibility(new int[]{1, 4, 1, 2}));
    }

    private static boolean checkPossibility(int[] nums) {
        if (nums.length == 1) {
            return true;
        }

        boolean first = false;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                if (!first) {
                    first = true;
                    if (i - 1 < 0 || nums[i - 1] <= nums[i + 1]) {
                        nums[i] = nums[i + 1];
                    } else {
                        nums[i + 1] = nums[i];
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

}
