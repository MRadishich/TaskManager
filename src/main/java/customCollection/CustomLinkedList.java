package main.java.customCollection;

import java.util.*;

public class CustomLinkedList<E> implements Iterable<E> {
    int size;
    Node<E> head;
    Node<E> tail;

    HashMap<E, Node<E>> map;

    public CustomLinkedList() {
        map = new HashMap<>();
    }

    Node<E> getHead() {
        return head;
    }

    Node<E> getTail() {
        return tail;
    }

    public void add(E e) {
        remove(e);

        Node<E> newNode = linkLast(e);

        map.put(e, newNode);
    }

    public void remove(E e) {
        if (map.containsKey(e)) {
            removeNode(map.get(e));
            map.remove(e);
        }
    }

    private Node<E> linkLast(E e) {
        final Node<E> t = tail;
        final Node<E> newNode = new Node<>(t, e, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        size++;
        return newNode;
    }

    private void removeNode(Node<E> e) {
        Node<E> next = e.next;
        Node<E> prev = e.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            e.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            e.next = null;
        }

        size--;
    }

    public void clear() {
        for (CustomLinkedList.Node<E> x = head; x != null; ) {
            CustomLinkedList.Node<E> tail = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = tail;
        }

        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomLinkedListIterator(this);
    }

    private static class Node<E> {
        Node<E> next;
        Node<E> prev;
        E item;

        Node(Node<E> prev, E value, Node<E> next) {
            this.item = value;
            this.prev = prev;
            this.next = next;
        }

        Node<E> getNext() {
            return next;
        }

        E getItem() {
            return item;
        }
    }

    class CustomLinkedListIterator implements Iterator<E> {
        Node<E> current;

        public CustomLinkedListIterator(CustomLinkedList<E> list) {
            current = list.getHead();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E item = current.getItem();
            current = current.getNext();

            return item;
        }
    }
}
