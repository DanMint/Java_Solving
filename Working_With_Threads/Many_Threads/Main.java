package com.company;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;



public class Main {
    public static void main(String[] args) {
        /// here I want to store the threads and use them when the prev threads ends
        ArrayList<Thread> allThreads = new ArrayList<>(10);

        thread t = new thread(0, 10);
        Thread t0 = new Thread(t);
        allThreads.add(t0);

        int toPass = t.get();
        System.out.println(toPass);

        for (int i = 1; i < 11; i ++) {
            thread t1 = new thread(i, -1);
            Thread tTemp = new Thread(t1);
            allThreads.add(tTemp);
        }

        int toSet;

        for (int i = 0; i < allThreads.size(); i ++) {
            allThreads.get(i).start();
        }

    }
}
