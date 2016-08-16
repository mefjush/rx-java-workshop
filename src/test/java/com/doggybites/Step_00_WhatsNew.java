package com.doggybites;

import org.junit.Test;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Step_00_WhatsNew {

    private Executor executor = Executors.newFixedThreadPool(2);

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
        @Override
        public synchronized String toString(String charsetName) throws UnsupportedEncodingException {
            String out = super.toString(charsetName);
            System.out.print(out);
            return out;
        }
    };

    private PrintStream out = new PrintStream(outputStream);

    private IntStream intervalsStream = IntStream.range(1, 10).map(i -> sleep(i));
    private Observable<Integer> intervalsObservable = Observable.range(1, 10).map(i -> sleep(i));

    @Test
    public void stream_is_blocking() throws UnsupportedEncodingException {
        intervalsStream.limit(3).forEach(out::println);

        assertThat(outputStream.toString("UTF-8"), is("1\n2\n3\n")); //TODO fix the assertion
    }

    @Test
    public void observable_is_async() throws UnsupportedEncodingException {
        intervalsObservable.limit(3).subscribeOn(Schedulers.io()).subscribe(out::println);  //TODO fix by adding one method call in this line

        assertThat(outputStream.toString("UTF-8"), is(""));
    }

    @Test(expected = IllegalStateException.class)
    public void stream_can_be_used_once() throws UnsupportedEncodingException {
        IntStream limited = intervalsStream.limit(3);
        limited.forEach(out::println);
        intervalsStream.forEach(out::println);

        //TODO fix the assertion
    }

    @Test
    public void observable_can_be_reused() throws UnsupportedEncodingException {
        Observable<Integer> limited = intervalsObservable.limit(3);
        limited.subscribe(out::println);
        intervalsObservable.subscribe(out::println);

        assertThat(outputStream.toString("UTF-8"), is("1\n2\n3\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n"));
    }

    @Test(timeout = 2000)
    public void callbacks_are_also_async() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        callbackSleep(10, i -> {
            System.out.println(i);
            //TODO
            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Test(timeout = 3000)
    public void combine_2_callbacks_together() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger sum = new AtomicInteger();

        callbackSleep(2, i -> {
            callbackSleep(5, j -> {
                //TODO
                System.out.println(i);
                countDownLatch.countDown();
                sum.addAndGet(i + j);
            });
        });

        countDownLatch.await();
        assertThat(sum.get(), is(7));
    }

    @Test(timeout = 3000)
    public void combine_2_observables_together() throws InterruptedException {
        Observable<Integer> delayed2 = Observable.just(2).map(i -> sleep(i));
        Observable<Integer> delayed5 = Observable.just(5).map(i -> sleep(i));

        Observable<Integer> sum = delayed2.flatMap(i -> delayed5.map(j -> i + j)); //TODO

        assertThat(sum.toBlocking().first(), is(7));
    }

    private int sleep(int i) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }

    private void callbackSleep(int i, Consumer<Integer> callback) {
        executor.execute(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.accept(i);
        });
    }
}
