package ru.fizteh.fivt.students.fminkin.threads;


/**
 * Created by Федор on 19.12.2015.
 */


public class Rhymes {
    private Integer n;
    private Integer last;

    class Rhyme implements Runnable {

        private final Integer i;

        Rhyme(Integer number) {
            i = number;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (Rhyme.this) {
                    while ((last + 1) % n != i) {
                        try {
                            Rhyme.this.wait();
                        } catch (InterruptedException e) {
                            System.err.printf("Interrupted %d\n", i);
                            return;
                        }
                    }
                    System.out.printf("Thread %d\n", (i + 1));
                    last = i;
                    Rhyme.this.notifyAll();
                }
            }
        }
    }

    public void load(int threads) {
        n = threads;
        last = n - 1;
        for (int i = 0; i < n; ++i) {
            (new Thread(new Rhyme(i))).start();
        }
    }
    public static void main(String[] args) {
       (new Rhymes()).load(Integer.parseInt(args[0]));
    }
}
