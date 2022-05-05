package datastructure;

import java.util.Collections;

public class UDArray <T> {
    private int capacity;
    private int size;
    private Object[] container;
    
    public UDArray() {
        this.capacity = 5;
        this.size = 0;
        this.container = new Object[this.capacity];
    }

    private void resize() {
        this.capacity = this.capacity * 2;
        Object[] arr = new Object[capacity];
        int size = this.size;
        for (int i = 0; i < size; i++) {
            arr[i] = container[i];
        }
        this.container = arr;
        System.out.println("resized");
    }

    public void add(T data) {
        if (size == capacity-2) {
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

}