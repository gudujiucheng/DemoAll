package com.example.algorithm.jiegou;

import java.util.Iterator;

public class 栈_Stack<T> implements Iterable<T> {
    private Node first;
    private int N;

    //定义节点
    private class Node {
        T item;
        Node next;
    }


    //判断是否为空栈
    public boolean isEmpty() {
        return first==null;

    }

    //返回数据个数
    public int size() {
        return N;

    }

    //入栈,新进栈的数据永远在first处
    public void push(T item) {
        Node node = new Node();
        node.next = first;
        node.item = item;
        first = node;
        N++;
    }

    //出栈
    public T pop() {
        if (first != null) {
            first = first.next;
            N--;
            return first.item;
        } else {
            return null;
        }

    }
    //返回迭代器对象

    @Override
    public Iterator<T> iterator() {
        return new MyLinkedStackIterator();
    }

    //迭代的实现
    private class MyLinkedStackIterator implements Iterator<T> {
        //获取外部类的first对象
        private Node n = first;

        @Override
        public boolean hasNext() {
            return n != null;
        }

        @Override
        public T next() {
            T item = n.item;
            n = n.next;
            return item;
        }
    }

}
