package github.clyoudu.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leichen
 */
public class Q207 {

    public static void main(String[] args) {
        int[][] prerequisites = new int[][]{{0, 1}, {0, 2}, {1, 2}};
        int n = 3;
        System.out.println(canFinish(n, prerequisites));
    }

    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, int[]> inOutMap = new HashMap<>(8);
        for (int i = 0; i < numCourses; i++) {
            inOutMap.put(i, new int[2]);
        }

        List<int[]> edgeList = Arrays.stream(prerequisites).collect(Collectors.toList());

        // 统计各个节点的入度和出度
        // value[0]-入度，value[1]-出度
        for (int[] prerequisite : prerequisites) {
            int a = prerequisite[0];
            int b = prerequisite[1];

            inOutMap.get(a)[1] = inOutMap.get(a)[1] + 1;
            inOutMap.get(b)[0] = inOutMap.get(b)[0] + 1;
        }

        while (!edgeList.isEmpty()) {
            boolean deleted = false;

            Iterator<int[]> it = edgeList.iterator();
            while (it.hasNext()) {
                int[] entry = it.next();
                if (inOutMap.get(entry[0])[0] == 0) {
                    it.remove();
                    inOutMap.get(entry[0])[1] = inOutMap.get(entry[0])[1] - 1;
                    inOutMap.get(entry[1])[0] = inOutMap.get(entry[1])[0] - 1;
                    deleted = true;
                } else if (inOutMap.get(entry[1])[1] == 0) {
                    it.remove();
                    inOutMap.get(entry[1])[0] = inOutMap.get(entry[1])[0] - 1;
                    inOutMap.get(entry[0])[1] = inOutMap.get(entry[0])[1] - 1;
                    deleted = true;
                }
            }

            if (!deleted) {
                System.out.println(edgeList.stream().flatMapToInt(Arrays::stream).distinct().mapToObj(i -> i + "")
                    .collect(Collectors.joining(",")));
                return false;
            }
        }
        return true;
    }

}
