package com.example.algorithm.leetcode;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class L_21 {
    public static void main(String[] args) {

        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(4);
        log(l1);
        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(4);
        log(l2);
        System.out.println("\n我是分割线");
        log(mergeTwoLists(l1,l2));
    }


    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if(l1==null){
            return l2;
        }
        if(l2==null){
            return l1;
        }
        List<ListNode> listNodes = new ArrayList<>();
        listNodes.add(l1);
        while (l1.next!=null){
            listNodes.add(l1.next);
            l1 = l1.next;
        }
        listNodes.add(l2);
        while (l2.next!=null){
            listNodes.add(l2.next);
            l2 = l2.next;
        }
        Collections.sort(listNodes, new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                if(o1.val>o2.val){
                    return 1;
                }else if(o1.val<o2.val){
                    return -1;
                }else{
                    return 0;
                }

            }
        });

        ListNode head = listNodes.get(0);
        ListNode temp = head;
        for (int i = 1; i <listNodes.size() ; i++) {
            temp.next = listNodes.get(i);
            temp = temp.next;
        }


        return  head;

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
