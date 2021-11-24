package com.company;

public class thread implements Runnable{
    int val;
    int id;

    thread (int id, int val) {
        this.id = id;
        this.val = val;
    }

    @Override
    public void run() {
        if (id == 1) {
            val += 30;
        }
        System.out.println("Thr vlaue is : " + val);
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int getInt () {
        return val;
    }

    void setInt (int a) {
        val = a;
    }

}
