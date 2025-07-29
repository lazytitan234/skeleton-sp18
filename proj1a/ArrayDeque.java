public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    //first item goes to index 1 of items. addFirst adds to index 0.

    public ArrayDeque() {
        this.items = (T[]) new Object[8];
        this.size = 0;
        this.nextFirst = 0;
        this.nextLast = 1;
    }

    public void addFirst(T item) {
        if (this.size == this.items.length) {
            this.resize(this.items.length * 2);
            this.items[nextFirst] = item;
            this.nextFirst = this.nextFirst - 1;
            this.size++;
        } else {
            this.items[nextFirst] = item;
            if (this.nextFirst == 0) {
                this.nextFirst = this.items.length - 1;
            } else {
                this.nextFirst--;
            }
            this.size++;
        }
    }

    public void addLast(T item) {
        if (this.size == this.items.length) {
            this.resize(items.length * 2);
            this.items[this.nextLast] = item;
            this.nextLast = this.nextLast + 1;
            this.size++;
        } else {
            this.items[nextLast] = item;
            if (this.nextLast == this.items.length - 1) {
                this.nextLast = 0;
            } else {
                this.nextLast++;
            }
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
        for (int i = 0; i < this.size; i++) {
            int index = (this.nextFirst + 1 + i) % items.length;
            System.out.print(items[index] + " ");
        }
        System.out.println();
    }


    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        T removed = this.items[(nextFirst + 1) % this.items.length];
        this.items[(this.nextFirst + 1) % this.items.length] = null;
        this.nextFirst = (this.nextFirst + 1) % this.items.length;
        if (this.size != 0) {
            this.size--;
        }
        if (this.items.length >= 16 && (this.size * 1.0 / this.items.length) < 0.25) {
            this.resize((int) (0.5 * this.items.length));
        }
        return removed;
    }

    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        T removed;
        if (this.nextLast == 0) {
            removed = this.items[this.items.length - 1];
            this.items[this.items.length - 1] = null;
            this.nextLast = this.items.length - 1;
        } else {
            removed = this.items[this.nextLast - 1];
            this.items[this.nextLast - 1] = null;
            this.nextLast--;
        }
        if (this.size != 0) {
            this.size--;
        }
        if (this.items.length >= 16 && (this.size * 1.0 / this.items.length) < 0.25) {
            this.resize((int) (0.5 * this.items.length));
        }
        return removed;
    }

    public T get(int index) {
        if (index >= this.size) {
            return null;
        }
        int trueIndex = ((this.nextFirst + 1) % this.items.length + index) % this.items.length;
        return this.items[trueIndex];
    }

    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        for (int i = 0; i < this.size; i++) {
            newArray[i] = this.items[(this.nextFirst + 1 + i) % this.items.length];
        }
        this.items = newArray;
        this.nextFirst = this.items.length - 1;
        this.nextLast = this.size;
    }
}
