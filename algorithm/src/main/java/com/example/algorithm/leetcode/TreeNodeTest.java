package com.example.algorithm.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 树
 * @Author: canzhang
 * @CreateDate: 2020/5/25 15:18
 * 原文链接：https://blog.csdn.net/qq_39240270/java/article/details/88614859
 */
public class TreeNodeTest {

    public static void main(String[] args) {
        TreeNode root =  new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(6);

        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);

        root.right.right = new TreeNode(8);

        preorder(root);

    }




    public static List<Integer> list = new ArrayList<>();

    //1.前序遍历    前序遍历是根节点首先输出，然后左子树输出，最后右子树输出
    public static void preorder(TreeNode root) {
        if (root != null) {
//            list.add(root.val);
            System.out.println("节点："+root.val);
            preorder(root.left);
            preorder(root.right);
        }
    }

    //2.1中序遍历,递归实现 中序遍历是左子树先输出，根节点在中间输出，右子树最后输出；
    public void inorder1(TreeNode root) {
        if (root != null) {
            inorder(root.left);
            System.out.println("节点："+root.val);
//            list.add(root.val);
            inorder(root.right);
        }
    }

    //2.2迭代和栈实现
    public void inorder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            list.add(root.val);
            root = root.right;
        }
    }

    //3.后序遍历  后续遍历是左子树，右子树，最后根节点最后输出。
    public void backorder(TreeNode root) {
        if (root != null) {
            backorder(root.left);
            backorder(root.right);
//            list.add(root.val);
            System.out.println("节点："+root.val);
        }
    }


    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

}
