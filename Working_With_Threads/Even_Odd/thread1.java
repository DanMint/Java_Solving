package com.company;

import java.util.ArrayList;

public class thread1 implements Runnable {
    int processID;
    ArrayList<Integer> values = new ArrayList<>();

    thread1 (int num, ArrayList<Integer> a) {
        processID = num;
        values = a;
    }

    @Override
    public void run() {
        for (Integer value : values) {
            if (value % 2 != 0)
                System.out.println(value + " The thread used is: " + processID);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
