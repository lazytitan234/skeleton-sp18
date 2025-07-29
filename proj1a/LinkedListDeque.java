public class LinkedListDeque<T>{

    private class Node {
        public T item;
        public Node prev;
        public Node next;
        public Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private int size;
    private Node sentinal;

    public LinkedListDeque() {
        this.size = 0;
        this.sentinal = new Node(null, null, null);
        this.sentinal.prev = this.sentinal;
        this.sentinal.next = this.sentinal.prev;
    }

    public void addFirst(T item) {
        if (this.size == 0) {
            Node newNode = new Node(item, sentinal, sentinal);
            this.sentinal.next = newNode;
            this.sentinal.prev = newNode;
            this.size++;
        } else {
            Node newNode = new Node(item, sentinal, sentinal.next);
            this.sentinal.next.prev = newNode;
            this.sentinal.next = newNode;
            this.size++;
        }
    }

    public void addLast(T item) {
        if (this.size == 0) {
            this.addFirst(item);
        } else {
            Node newNode = new Node(item, this.sentinal.prev, this.sentinal);
            this.sentinal.prev.next = newNode;
            this.sentinal.prev = newNode;
            this.size++;
        }
    }

    public boolean isEmpty() {
        if (this.size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        Node currentNode = this.sentinal;
        for (int i = 0; i < this.size; i++) {
            currentNode = currentNode.next;
            System.out.print(currentNode.item + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        Node first = this.sentinal.next;
        if (first == this.sentinal) {
            return null;
        }
        if (this.size == 1) {
            this.sentinal.next = this.sentinal;
            this.sentinal.prev = this.sentinal;
        } else {
            this.sentinal.next.next.prev = this.sentinal;
            this.sentinal.next = first.next;
        }
        if (this.size != 0) {
            this.size--;
        }
        return first.item;
    }

    public T removeLast() {
        Node last = this.sentinal.prev;
        if (last == this.sentinal) {
            return null;
        }
        if (this.size == 1) {
            this.sentinal.next = this.sentinal;
            this.sentinal.prev = this.sentinal;
        } else {

            this.sentinal.prev.prev.next = this.sentinal;
            this.sentinal.prev = this.sentinal.prev.prev;
        }
        if (this.size != 0) {
            this.size--;
        }
        return last.item;
    }

    public T get(int index) {
        Node currentNode = this.sentinal.next;
        for (int i = 0; i < this.size; i++) {
            if (i == index) {
                return currentNode.item;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public T getRecursive(int index) {
        Node currentNode = this.sentinal.next;
        return this.helpergetRecursive(currentNode, index);
    }

    private T helpergetRecursive(Node node, int index) {
        if (node == this.sentinal) {
            return null;
        }
        if (index == 0) {
            return node.item;
        }
        return helpergetRecursive(node.next, index - 1);
    }
}