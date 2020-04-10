package com.example.algorithm.leetcode;


public class L_206 {
    public static void main(String[] args) {

        ListNode head = new ListNode(1);
        int i = 1;
        ListNode last = head;
        while (i <= 1) {
            i++;
            ListNode temp = new ListNode(i);
            last.next = temp;
            last = temp;
        }
        log(head);
        System.out.println("\n我是分割线");
//        log(reverseList(head));
        log(reverseListDigui(head));
    }


    public static ListNode reverseList(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode tempNext = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tempNext;
        }
        return pre;
    }


    public static ListNode reverseListDigui(ListNode head) {
        if(head==null){
            return null;
        }
        ListNode cur = head.next;
        head.next = null;
        return reverse(cur, head);
    }

    public static ListNode reverse(ListNode cur, ListNode pre) {
        if (cur == null) {
            return pre;
        }
        ListNode nextNode = cur.next;
        cur.next = pre;
        return reverse(nextNode, cur);
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
