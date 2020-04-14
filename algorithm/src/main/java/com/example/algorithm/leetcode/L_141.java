package com.example.algorithm.leetcode;


import java.util.HashMap;

public class L_141 {
    public static void main(String[] args) {

        ListNode head = new ListNode(1);
        int i = 1;
        ListNode last = head;
        ListNode cycleNode = null;
        while (i <= 4) {
            i++;

            ListNode temp = new ListNode(i);
            if (i == 2) {
                cycleNode = temp;
            }
            last.next = temp;
            last = temp;
        }
        last.next = cycleNode;
//        log(head);
        System.out.println("\n我是分割线");
        System.out.println(hasCycle(head));
    }


    /**
     * 通过map 记录  简单方式
     *
     * @param head
     * @return
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode node = head.next;
        if (node == null) {
            return false;
        }

        HashMap<ListNode, Integer> objectObjectHashMap = new HashMap<>();//存放所有结点
        int index = 1;
        objectObjectHashMap.put(head, index);
        while (node != null) {
            if (objectObjectHashMap.containsKey(node)) {
                Integer pos = objectObjectHashMap.get(node);
                System.out.println("对应结点：" + node.val + " 索引：" + pos);
                return true;
            }
            objectObjectHashMap.put(node, index++);
            node = node.next;
        }
        return false;

    }

//    public boolean hasCycle(ListNode head) {
//        Set<ListNode> nodesSeen = new HashSet<>(); //FIXME 人家用的hash set  更为简洁
//        while (head != null) {
//            if (nodesSeen.contains(head)) {
//                return true;
//            } else {
//                nodesSeen.add(head);
//            }
//            head = head.next;
//        }
//        return false;
//    }
//
//    作者：LeetCode
//    链接：https://leetcode-cn.com/problems/linked-list-cycle/solution/huan-xing-lian-biao-by-leetcode/
//    来源：力扣（LeetCode）
//    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

    public static boolean hasCycle02(ListNode head) {
        if (head == null || head.next == null || head.next.next == null) {
            return false;
        }
        ListNode slowNode = null;//每次走一步
        ListNode fastNode = null;//每次走两步

        //如果存在环，那么两个指针都会进入环内，并且一个快一个慢，终会相遇
        slowNode = head.next;
        fastNode = head.next.next;
        while (fastNode != null && slowNode != fastNode) {
            slowNode = slowNode.next;
            fastNode = fastNode.next.next;
        }
        if(fastNode==null){
            return false;
        }else{
            return true;
        }


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
