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

    public Node<E> getHead() {
        return head;
    }

    public Node<E> getTail() {
        return tail;
    }

    public void add(E e) {
        remove(e);
        Node<E> newNode = linkLast(e);
        map.put(e, newNode);
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

    public void remove(E e) {
        if (map.containsKey(e)) {
            removeNode(map.get(e));
            map.remove(e);
        }
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

    public List<E> getTask() {
        List<E> list = new ArrayList<>(size);
        for (Node<E> x = head; x != null; x = x.next) {
            list.add(x.item);
        }
        return list;
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

        public Node<E> getNext() {
            return next;
        }

        public E getItem() {
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

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
