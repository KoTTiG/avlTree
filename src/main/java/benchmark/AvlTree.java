package benchmark;
import java.util.*;

public class AvlTree<T extends Comparable<T>> implements Set<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;
        int height;

        Node(T value) {
            this.value = value;
            height = 1;
        }

    }

    private Node<T> root = null;
    private int size = 0;

    private int height(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private int bFactor(Node<T> node) {
        return height(node.right) - height(node.left);
    }

    private void newHeight(Node<T> node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> head = node.left;
        node.left = head.right;
        head.right = node;
        newHeight(node);
        newHeight(head);
        return head;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> head = node.right;
        node.right = head.left;
        head.left = node;
        newHeight(node);
        newHeight(head);
        return head;
    }

    private Node<T> balance(Node<T> node) {
        newHeight(node);
        if (bFactor(node) == 2) {
            if (bFactor(node.right) < 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        } else if (bFactor(node) == -2) {
            if (bFactor(node.left) > 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        } else {
            return node;
        }
    }

    @Override
    public boolean contains(Object o) {
        if (root == null|| o == null || o.getClass() != root.value.getClass()) return false;
        return contains(root, (T) o);

    }

    private boolean contains(Node<T> node, T value) {
        if (node == null) {
            return false;
        }
        int comparison = value.compareTo(node.value);
        if (comparison > 0) {
            return  contains(node.right, value);
        } else if (comparison < 0) {
            return contains(node.left, value);
        } else {
            return true;
        }
    }

    boolean wasAdded;

    @Override
    public boolean add(T value) {
        wasAdded = false;
        root = add(root, value);
        return wasAdded;
    }

    private Node<T> add(Node<T> node, T value) {

        if (node == null) {
            size++;
            wasAdded = true;
            return node = new Node<T>(value);
        }
        int comparison = value.compareTo(node.value);
        if (comparison > 0) {
            node.right = add(node.right, value);
        } else if (comparison < 0) {
            node.left = add(node.left, value);
        } else {
            return node;
        }
        return balance(node);

    }

    boolean wasRemoved;

    @Override
    public boolean remove(Object o) {
        if (root == null|| o == null || o.getClass() != root.value.getClass()) return false;
        wasRemoved = false;
        root = remove(root, (T) o);
        return wasRemoved;
    }

    private Node<T> remove(Node<T> node, T value) {
        if (node == null) {
            return null;
        }
        int comparison = value.compareTo(node.value);
        if (comparison > 0) {
            node.right = remove(node.right, value);
        } else if (comparison < 0) {
            node.left = remove(node.left, value);
        } else {
            wasRemoved = true;
            Node<T> l = node.left;
            Node<T> r = node.right;
            Node<T> min = findMin(r);
            if (min != null) {
                min.right = removeMin(r);
                min.left = l;
                return balance(min);
            }
            return null;
        }
        return balance(node);

    }

    private Node<T> findMin(Node<T> node) {
        if (node == null) return null;
        return node.left == null ? node : findMin(node.left);
    }

    private Node<T> removeMin(Node<T> node) {
        if (node.left == null) return node.right;
        node.left = removeMin(node.left);
        return balance(node);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        while(c.iterator().hasNext()){
            if (!this.contains(c.iterator().next())) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean hasChanged = false;
        while(c.iterator().hasNext()){
            if (this.add(c.iterator().next())) hasChanged = true;
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean hasChanged = false;
        while(c.iterator().hasNext()){
            if (this.remove(c.iterator().next())) hasChanged = true;
        }
        return true;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++){
            array[i] = this.iterator().next();
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length >= size){
            for (int i = 0; i < size; i++){
                a[i] = (T) this.iterator().next();
            }
            return a;
        }else {
            return (T[]) this.toArray();
        }
    }

    public Iterator<T> iterator() {
        return new AvlTreeIterator();
    }

    public class AvlTreeIterator implements Iterator<T> {
        private Node<T> next;
        int index = 0;
        boolean callNext = false;

        @Override
        public boolean hasNext() {
            if (root == null) return false;
            Node<T> first = null;
            if (next == null && index == 0) first = findMin(root);
            if (first != null) return findNext(first) != null;
            return findNext(next) != null;
        }

        private Node<T> findNext(Node<T> node) {
            Node<T> current = root;
            Node<T> successor = null;
            while (current != null) {
                if (current.value.compareTo(node.value) > 0) {
                    successor = current;
                    current = current.left;
                } else current = current.right;
            }
            return successor;
        }

        @Override
        public T next() {
            if (index == size) throw new NoSuchElementException();
            if (next == null && index == 0) {
                next = findMin(root);
            } else {
                next = findNext(next);
            }
            index++;
            callNext = true;
            return next.value;

        }

        @Override
        public void remove() {
            if (!callNext) throw new IllegalStateException();
            AvlTree.this.remove(next.value);
            index--;
            callNext = false;
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    public int height() {
        return root.height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvlTree<?> avlTree = (AvlTree<?>) o;
        return size == avlTree.size &&
                Objects.equals(root, avlTree.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, size);
    }
}
