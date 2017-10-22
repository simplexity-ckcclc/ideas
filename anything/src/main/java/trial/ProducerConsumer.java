package trial;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ckcclc on 02/09/2017.
 */
public class ProducerConsumer {

    public static void main(String[] args) {
        Queue<Integer> queue = new ArrayBlockingQueue<Integer>(3);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        producer.start();
        consumer.start();
    }
}


class Producer extends Thread {

    private Queue queue;

    public Producer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {


        while (true) {
            synchronized (queue) {
                while (queue.size() == 3) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                queue.offer(new Integer(1));
                queue.notifyAll();
                System.out.println("Produce: queue size =" + queue.size());
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {

    private Queue queue;

    public Consumer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {


        while (true) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                queue.remove();
                queue.notifyAll();
                System.out.println("Consumer: queue size =" + queue.size());
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Item {

    private AtomicInteger number = new AtomicInteger(0);

    public int getNumber() {
        return number.get();
    }

    public void incrNumber() {
        this.number.incrementAndGet();
    }
}
