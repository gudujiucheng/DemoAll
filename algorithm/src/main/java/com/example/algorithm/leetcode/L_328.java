package com.example.algorithm.leetcode;


public class L_328 {
    public static void main(String[] args) {

        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(3);
        l1.next.next.next = new ListNode(4);
        l1.next.next.next.next = new ListNode(5);
        log(l1);
        System.out.println("\n我是分割线");
        log(oddEvenList(l1));
    }


    public static ListNode oddEvenList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode jiNode = head;
        ListNode ouNode = null;
        ListNode ouHead = null;
        ListNode temp = head.next;
        int index = 1;
        while (temp != null) {//遍历
            index++;
            if (index % 2 == 0) {//偶数
                if (ouNode == null) {
                    ouHead = ouNode = temp;
                } else {
                    ouNode.next = temp;
                    ouNode = temp;
                }

            } else {//奇数
                jiNode.next = temp;
                jiNode = temp;
            }
            temp = temp.next;
        }
        if (ouNode != null)
            ouNode.next = null;//防止成环
        jiNode.next = ouHead;//拼接串串
        return head;

    }

    private static void log(ListNode head) {
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
