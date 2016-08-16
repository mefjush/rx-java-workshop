package com.doggybites;

import org.junit.Test;
import rx.Observable;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Step_03_Time {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
        @Override
        public void write(byte[] b) throws IOException {
            System.out.print(new String(b, "UTF-8"));
            super.write(b);
        }

        @Override
        public synchronized void write(byte[] b, int off, int len) {
            byte[] copy = Arrays.copyOfRange(b, off, len);
            System.out.print(new String(copy));
            super.write(b, off, len);
        }
    };

    private PrintStream out = new PrintStream(outputStream);

    private Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

    @Test
    public void ticks_3_times_every_even_second() throws InterruptedException, UnsupportedEncodingException {
        //TODO
        seconds.filter(i -> i % 2 == 0).take(3).toBlocking().forEach(out::println);
        //or with latch and onComplete

        assertThat(outputStream.toString("UTF-8"), is("0\n2\n4\n"));
    }

    @Test
    public void waits_2_seconds_and_then_generates_1_2_3() throws UnsupportedEncodingException {
        //TODO
        Observable.just(1, 2, 3).delay(2, TimeUnit.SECONDS).toBlocking().forEach(out::println);
        //or
        //seconds.take(2).skip(2).concatWith(Observable.just(1L, 2L, 3L)).toBlocking().forEach(out::println);

        assertThat(outputStream.toString("UTF-8"), is("1\n2\n3\n"));
    }

}
