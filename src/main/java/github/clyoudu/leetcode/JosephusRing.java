package github.clyoudu.leetcode;

/**
 * @author leichen
 * @date 2020/5/16 4:10 下午
 */
public class JosephusRing {

    public static void main(String[] args) {
        josephus(500000, 3);
    }

    private static void josephus (int num, int k) {
        if(num == 1) {
            System.out.println(1);
            return;
        }
        Node head = new Node();
        head.setNum(1);
        Node pre = head;
        for (int i = 2;i <= num; i++) {
            Node node = new Node();
            node.setNum(i);
            pre.setNext(node);
            node.setPre(pre);
            pre = node;
        }
        pre.setNext(head);
        head.setPre(pre);

        int left = num;
        Node curNode = head;
        int cuNum = 0;
        while (left > 1) {
            cuNum ++;
            if (cuNum == k) {
                curNode.getPre().setNext(curNode.getNext());
                curNode.getNext().setPre(curNode.getPre());
                cuNum = 0;
                left --;
            }
            curNode = curNode.getNext();
        }

        System.out.println(curNode.getNum());
    }

    static class Node {
        private int num;
        private Node next;
        private Node pre;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPre() {
            return pre;
        }

        public void setPre(Node pre) {
            this.pre = pre;
        }
    }

}
