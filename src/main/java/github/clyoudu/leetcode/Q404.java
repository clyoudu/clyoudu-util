package github.clyoudu.leetcode;

import javax.swing.tree.TreeNode;

/**
 * @author leichen
 */
public class Q404 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        TreeNode right = new TreeNode(20);
        root.right = right;
        right.left = new TreeNode(15);
        right.right = new TreeNode(7);

        System.out.println(sumOfLeftLeaves(root));
    }

    private static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        } else if (root.left == null && root.right == null) {
            return root.val;
        } else {
            int sum = 0;
            if (root.left != null) {
                sum += sumOfLeftLeaves(root.left, true);
            }
            if (root.right != null) {
                sum += sumOfLeftLeaves(root.right, false);
            }

            return sum;
        }
    }

    private static int sumOfLeftLeaves(TreeNode root, boolean left) {
        int sum = 0;
        if (left && root.left == null && root.right == null) {
            sum += root.val;
        } else {
            if (root.left != null) {
                sum += sumOfLeftLeaves(root.left, true);
            }
            if (root.right != null) {
                sum += sumOfLeftLeaves(root.right, false);
            }
        }
        return sum;
    }

    public static class TreeNode {

        int val;

        TreeNode left;

        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

}
