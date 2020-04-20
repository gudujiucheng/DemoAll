package com.example.algorithm.leetcode;


public class L_25 {
    public static void main(String[] args) {

        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(3);
        l1.next.next.next = new ListNode(4);
        l1.next.next.next.next = new ListNode(5);
        l1.next.next.next.next.next = new ListNode(6);
        l1.next.next.next.next.next.next = new ListNode(7);
        log("原始", l1);
        System.out.println("\n我是分割线");
        log("测试", reverseKGroup(l1, 3));
//        log(reverseNode(l1));
    }


    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        if (k == 1) {
            return head;
        }
        //首先分割链表
        int n = 0;
        ListNode lastHead = head;//记录起始点
        ListNode preHead = null;//上一个起始点，也就是上一个反转后的尾结点
        ListNode nextNode = head;
        ListNode reverseLastNode = null;//记录最后反转的那个头节点
        ListNode returnNode = null;
        while (nextNode != null) {
            n++;
            if (n == k) {//每次累计k个 就进行一次反转处理
                //反转前需要把尾部节点next置空,防止反转整个链表
                ListNode next = nextNode.next;
                nextNode.next = null;//截断链表
                if (returnNode == null) {//记录首节点
                    returnNode = nextNode;
                }
                reverseLastNode = lastHead;
                ListNode tempReverseNode = reverseNode(lastHead);
                log("反转：", tempReverseNode);//反转链表
                //反转后 首节点变成了尾结点，要连接一下接下来的节点，保证能够顺成
                if (preHead != null)
                    preHead.next = tempReverseNode;//衔接反转的点，这个容易错
                preHead = lastHead;
                lastHead = next;//更新起始点
                n = 0;//从头计算
                nextNode = next;
            } else {
                nextNode = nextNode.next;
            }


        }
        if (n > 0) {//表示尾部有不够k个的
//           lastHead 记录的反而是不够的头节点，那么只要在找出上一个lastHead，就能串起来了
            if (reverseLastNode != null)
                reverseLastNode.next = lastHead;
        }


        return returnNode;

    }

    private static ListNode reverseNode(ListNode lastHead) {
        ListNode nextNode = lastHead.next;//第二个
        lastHead.next = null;
        ListNode foreNode = lastHead;
        while (nextNode != null) {
            ListNode next = nextNode.next;
            nextNode.next = foreNode;//指向上一个
            foreNode = nextNode;
            nextNode = next;
        }
        return foreNode;

    }

    private static void log(String tips, ListNode head) {
        System.out.println(tips);
        while (head != null) {
            System.out.println(head.val);
            head = head.next;
        }
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
