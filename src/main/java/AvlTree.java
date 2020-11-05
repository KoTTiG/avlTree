import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AvlTree<T extends Comparable<T>> extends AbstractSet<T> {

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

    boolean wasAdded;

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

    public boolean remove(T value) {
        wasRemoved = false;
        root = remove(root, value);
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
        return node.left == null? node : findMin(node.left);
    }

    private Node<T> removeMin(Node<T> node) {
        if (node.left == null) return node.right;
        node.left = removeMin(node.left);
        return balance(node);
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
            while (current != null){
                if (current.value.compareTo(node.value) > 0) {
                    successor = current;
                    current = current.left;
                }
                else current = current.right;
            }
            return successor;
        }

        @Override
        public T next() {
            if(index == size) throw new NoSuchElementException();
            if (next == null && index == 0) {
                next = findMin(root);
            }else {
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

    public int height() {
        return root.height;
    }


}
