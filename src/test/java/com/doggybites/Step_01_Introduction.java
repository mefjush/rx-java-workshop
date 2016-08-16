package com.doggybites;

import org.junit.Test;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class Step_01_Introduction {
    
    private Observable<Integer> ints = Observable.just(1, 3, 8);

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
        @Override
        public synchronized String toString(String charsetName) throws UnsupportedEncodingException {
            String out = super.toString(charsetName);
            System.out.print(out);
            return out;
        }
    };

    private PrintStream out = new PrintStream(outputStream);

    private ByteArrayOutputStream silentOutputStream = new ByteArrayOutputStream();
    private PrintStream silentOut = new PrintStream(silentOutputStream);

    @Test
    public void print_ints_out() throws UnsupportedEncodingException {
        //TODO
        ints.subscribe(out::println);

        assertThat(outputStream.toString("UTF-8"), is("1\n3\n8\n"));
    }

    @Test
    public void prints_ints_multiplied_by_2() throws UnsupportedEncodingException {
        //TODO
        ints.map(i -> i * 2).subscribe(out::println);

        assertThat(outputStream.toString("UTF-8"), is("2\n6\n16\n"));
    }

    @Test
    public void prints_the_last_int() throws UnsupportedEncodingException {
        //TODO
        ints.last().subscribe(out::println);

        assertThat(outputStream.toString("UTF-8"), is("8\n"));
    }

    @Test
    public void prints_only_odd_ints() throws UnsupportedEncodingException {
        //TODO
        ints.filter(i -> i % 2 != 0).subscribe(out::println);

        assertThat(outputStream.toString("UTF-8"), is("1\n3\n"));
    }

    @Test
    public void prints_sum_of_ints() throws UnsupportedEncodingException {
        //TODO
        ints.reduce((i, j) -> i + j).subscribe(out::println);

        assertThat(outputStream.toString("UTF-8"), is("12\n"));
    }

    @Test
    public void can_i_count_to_3() throws UnsupportedEncodingException {
        AtomicInteger counter = new AtomicInteger();

        Observable<Integer> observable123 = ints.map(i -> counter.incrementAndGet());
        observable123.subscribe(silentOut::println);

        assertTrue(silentOutputStream.toString("UTF-8").equals("1\n2\n3\n")); //TODO
    }

    @Test
    public void can_i_calculate_the_sum() throws UnsupportedEncodingException {
        AtomicInteger counter = new AtomicInteger();
        Observable<Integer> observable123 = ints.map(i -> counter.incrementAndGet());
        Observable<Integer> sum = observable123.reduce((a, b) -> a + b);

        sum.subscribe(silentOut::println);

        assertTrue(silentOutputStream.toString("UTF-8").equals("6\n")); //TODO
    }

    @Test
    public void can_i_calculate_the_sum_twice() throws UnsupportedEncodingException {
        AtomicInteger counter = new AtomicInteger();
        Observable<Integer> observable123 = ints.map(i -> counter.incrementAndGet());
        Observable<Integer> sum = observable123.reduce((a, b) -> a + b);

        sum.subscribe(silentOut::println);
        sum.subscribe(s -> silentOut.println("Sum:" + s));

        assertTrue(silentOutputStream.toString("UTF-8").equals("6\nSum:15\n")); //TODO
    }
}
