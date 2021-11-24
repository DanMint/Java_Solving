package com.company;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;



public class Main {
    public static void main(String[] args) throws InterruptedException {
        thread a = new thread(0, 0);
        Thread t = new Thread(a);

        t.start();

        while (true) {
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("Current get is: " + a.get());
            if (a.get() == 10) {
                a.stop();
                System.out.println("Finito Amigo");
                break;
            }
        }

    }
}
