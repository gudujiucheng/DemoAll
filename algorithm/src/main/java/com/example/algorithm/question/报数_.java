package com.example.algorithm.question;


/**
 *  参考 https://blog.csdn.net/mark555/article/details/22402705
 *  约瑟夫环问题（还有一种找规律的公式解法更简单）
 */

public class 报数_ {


    public static void main(String[] args) {
        String str = "12345";
        Node headerNode = initNode(str);
        getTheLastNode(headerNode,2);

    }



    static class Node {
        String value;
        Node next;

        Node() {

        }
    }


    //初始化循环链表
    private static Node initNode(String str) {

        Node headNode = new Node();
        headNode.value = String.valueOf(str.charAt(0));

        Node tempNode = headNode;
        for (int i = 1; i < str.length(); i++) {
            Node node = new Node();
            node.value = String.valueOf(str.charAt(i));
            //赋值next
            tempNode.next = node;
            //继续指向下一个节点
            tempNode = node;

        }

        //最后一个节点关联头部节点，形成环形
        tempNode.next = headNode;
        return headNode;

    }


    private static void getTheLastNode(Node headerNode, int m) {
        //记录循环开始的头结点
        Node first = headerNode;
        while (first!=first.next) {//只要还有下一个节点就持续循环
            for (int i = 1; i <m-1 ; i++) {//循环报数，并调整first节点
                //循环结束 这个就是要删除的节点的前一个节点
                first = first.next;
            }

            //first后一个节点需要删除。所以要重建链接
            first.next = first.next.next;
            //然后循环是从下一个节点重新开始，更改first指向
            first = first.next;

        }

        System.out.print("最后一个节点是："+first.value);

    }
}
