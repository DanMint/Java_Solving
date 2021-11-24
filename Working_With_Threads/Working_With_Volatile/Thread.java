package com.company;

public class thread implements Runnable{
    int value;
    int id;
    volatile boolean play = true;

    thread (int id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void run() {
        System.out.println("You are using thread no: " + id);
        System.out.println("Value before using start is: " + value);
        while (play) {
            value += 1;
            set(value);
            System.out.println(value);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    int get () {
        return value;
    }

    void set (int a) {
        this.value = a;
    }

    void stop() {
        play = false;
    }
}
