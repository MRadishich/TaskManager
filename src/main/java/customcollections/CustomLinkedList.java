package main.java.customcollections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

public class CustomLinkedList<E> implements Iterable<E> {
    protected int size;
    private Node<E> head;
    private Node<E> tail;

    private final HashMap<E, Node<E>> map;

    public CustomLinkedList() {
        map = new HashMap<>();
    }

    protected Node<E> getHead() {
        return head;
    }

    protected Node<E> getTail() {
        return tail;
    }

    public void add(E e) {
        remove(e);

        Node<E> newNode = linkLast(e);

        map.put(e, newNode);
    }

    public void remove(E e) {
        Optional.ofNullable(map.remove(e)).ifPresent(this::removeNode);
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

    public void clear() {
        map.clear();
        head = tail = null;
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        while (it.hasNext()) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (it.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.append(']').toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new CustomLinkedListIterator(this);
    }

    private static class Node<E> {
        private Node<E> next;
        private Node<E> prev;
        private E item;

        protected Node(Node<E> prev, E value, Node<E> next) {
            this.item = value;
            this.prev = prev;
            this.next = next;
        }

        protected Node<E> getNext() {
            return next;
        }

        protected E getItem() {
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
