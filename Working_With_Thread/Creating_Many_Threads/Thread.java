package com.company;

public class thread implements Runnable{
    int value;
    int id;

    thread (int id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void run() {
        System.out.println("You are using thread no: " + id);
        System.out.println("Value before using start is: " + value);
        if (id != 0) {
            value = 10;
        }
        System.out.println("Value after using start is: " + value);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int get() {
        return value;
    }

    void set(int val) {
    }
}
