package com.example.algorithm.leetcode;

/**
 * https://leetcode-cn.com/problems/kth-node-from-end-of-list-lcci/
 */
public class M_1423 {
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
        System.out.println(kthToLast(head, 2));

    }

    private static void log(ListNode head) {
        while (head != null) {
            System.out.println(head.val);
            head = head.next;
        }
    }

    public static int kthToLast(ListNode head, int k) {
        if (head == null) {
            return -1;//错误
        }
        int n = 1;
        ListNode next = head.next;
        while (next != null) {//计算总长度
            n++;
            next = next.next;
        }

//        n-k+1 位置的节点，就是倒数第K个节点
        int temp = 1;
        if (temp == n - k + 1) {//获取倒数
            return head.val;
        }
        ListNode nextNode = head.next;
        while (nextNode != null) {
            temp++;
            if (temp == n - k + 1) {//获取倒数
                break;
            }
            nextNode = nextNode.next;
        }
        if (nextNode == null) {
            return -1;//错误，未找到对应点
        }
        return nextNode.val;

    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    /**
     * 高手解法 双指针
     * <p>
     * 比较典型的双指针游走题目，设有两个指针 p,q ：
     * <p>
     * 初始时，两个指针均指向 head。
     * 先将 q 向后移动 k 次。此时p，q的距离为 k。
     * 同时移动 p，q, 直到 q 指向 nullptr。此时p->val即为答案。
     * <p>
     * 作者：banfeipeng
     * 链接：https://leetcode-cn.com/problems/kth-node-from-end-of-list-lcci/solution/tu-jie-shuang-zhi-zhen-you-zou-javachao-guo-100de-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param head
     * @param k
     * @return
     */
    public int kthToLastX(ListNode head, int k) {
        ListNode p = head;
        for (int i = 0; i < k; i++) {
            p = p.next;
        }

        while (p != null) {
            p = p.next;
            head = head.next;
        }

        return head.val;
    }


}
