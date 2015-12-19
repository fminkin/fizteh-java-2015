package ru.fizteh.fivt.students.fminkin.threads;

import java.util.Random;

/**
 * Created by Федор on 19.12.2015.
 */

public class Muster {
    private int count = 0;
    private int n;
    private boolean ready;
    private Random random;
    private int happened = 0;
    static final int SEED = 505;
    static final double PROBABILTY = 0.1;
    class MusterThread implements Runnable {
        private int last;
        MusterThread() {
            last = 0;
            count++;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (Muster.this) {
                    while (last == happened) { //does it's evil thing and waits for all threads
                        if (count == n && ready) {
                            return;
                        }
                        try {
                            Muster.this.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Interrupted\n");
                            return;
                        }
                    }
                    last++;
                    if (random.nextDouble() < PROBABILTY) {
                        System.out.println("NO");
                        ready = false;
                    } else {
                        System.out.println("YES");
                    }
                    count++;
                    MusterThread.this.notifyAll();
                }
            }
        }
    }

    public void main(String[] args) {
        n = Integer.parseInt(args[0]);
        random = new Random(SEED);

        for (int i = 0; i < n; ++i) {
            (new Thread(new MusterThread())).start();
        }

        while (true) {
            synchronized (this) {
                while (count != n) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.err.printf("Interrupted\n");
                        return;
                    }
                }
                if (ready) {
                    return;
                }

                System.out.println("Are you ready?");
                count = 0;
                happened++;
                ready = true;
                notifyAll();
            }
        }

    }
}
