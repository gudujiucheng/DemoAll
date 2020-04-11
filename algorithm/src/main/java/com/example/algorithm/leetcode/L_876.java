package com.example.algorithm.leetcode;


public class L_876 {
    public static void main(String[] args) {

        ListNode head = new ListNode(1);
        int i = 1;
        ListNode last = head;
        while (i <= 4) {
            i++;
            ListNode temp = new ListNode(i);
            last.next = temp;
            last = temp;
        }
        log(head);
        System.out.println("\n我是分割线");
//        log(reverseList(head));
        log(middleNode(head));
    }


    public static ListNode middleNode(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode nextNode = head.next;
        int n = 1;
        while (nextNode != null) {//计算总长度
            n++;
            nextNode = nextNode.next;
        }
        int index;
        if (n % 2 == 0) {//偶数
            index = n / 2 + 1;
        } else {//奇数
            index = (n + 1) / 2;
        }

        int temp =1;
        if(temp==index){
            return  head;
        }
        ListNode next = head.next;
        while (next!=null){
            temp++;
            if(temp==index){
                break;
            }
            next = next.next;
        }
        return  next;

    }


    /**
     * 高手做法
     * 我们可以继续优化方法二，用两个指针 slow 与 fast 一起遍历链表。slow 一次走一步，fast 一次走两步。那么当 fast 到达链表的末尾时，slow 必然位于中间。

     作者：LeetCode-Solution
     链接：https://leetcode-cn.com/problems/middle-of-the-linked-list/solution/lian-biao-de-zhong-jian-jie-dian-by-leetcode-solut/
     来源：力扣（LeetCode）
     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param head
     *
     * 我们可以继续优化方法二，用两个指针 slow 与 fast 一起遍历链表。slow 一次走一步，fast 一次走两步。那么当 fast 到达链表的末尾时，slow 必然位于中间。
     */

    public ListNode middleNodeX(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
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
