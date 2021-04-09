package github.clyoudu.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author leichen
 */
public class Q1431 {

    public static void main(String[] args) {
        System.out.println(StringUtils.join(kidsWithCandies(new int[]{2,3,5,1,3}, 3), ","));
        System.out.println(StringUtils.join(kidsWithCandies(new int[]{4,2,1,1,2}, 1), ","));
    }

    private static List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> result = new ArrayList<>();
        int max = Arrays.stream(candies).max().getAsInt();
        for (int candy : candies) {
            if (candy + extraCandies >= max) {
                result.add(true);
            } else {
                result.add(false);
            }
        }
        return result;
    }
}
