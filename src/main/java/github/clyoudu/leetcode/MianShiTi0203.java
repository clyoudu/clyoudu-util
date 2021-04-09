package github.clyoudu.leetcode;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author leichen
 */
public class MianShiTi0203 {

    public static void main(String[] args) {
        ListNode node = new ListNode(4);
        ListNode node1 = new ListNode(5);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(9);
        node2.next = node3;
        node1.next = node2;
        node.next = node1;

        deleteNode(node1);

        System.out.println(node.toString());
    }

    private static void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

     public static class ListNode {
          int val;
          ListNode next;
          ListNode(int x) { val = x; }

         @Override
         public String toString() {
             List<Integer> list = new ArrayList<>();

             ListNode node = this;
             while (node != null) {
                 list.add(node.val);
                 node = node.next;
             }

             return StringUtils.join(list, "->");
         }
     }

}
