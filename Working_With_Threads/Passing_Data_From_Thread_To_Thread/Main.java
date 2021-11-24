package com.company;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;



public class Main {
    public static void main(String[] args) throws InterruptedException {
        thread a1 = new thread(1, 10);
        Thread t1 = new Thread(a1);
        t1.start();
//        t1.join();
        thread a2 = new thread(2, 0);
        a2.setInt(a1.getInt());
        Thread t2 = new Thread(a2);
        t2.start();

    }
}
