package datastructure;

public class UDArray <T> {
    private int capacity = 1;
    private int size = 0;
    private Object[] container;
    
    public UDArray() {
        this.container = new Object[this.capacity];
    }

    private void resize() {
        this.capacity = this.capacity * 2;
        Object[] arr = new Object[capacity];

        for (int i = 0; i < this.size; i++) {
            arr[i] = container[i];
        }
        this.container = arr;
    }

    public void add(T data) {
        if (size == container.length) {
            resize();
        }
        container[size++] = data;
    }

    public T get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) container[index];
    }

    public void sort() {}
    //TODO: Implement the sort method

    public void print() {
        int size = this.size;
        for (int i = 0; i < size; i++) {
            System.out.println(this.get(i));
        }
    }

    public int getSize() {
        return size;
    }

    public Object[] getContainer() {
        return container;
    }
}