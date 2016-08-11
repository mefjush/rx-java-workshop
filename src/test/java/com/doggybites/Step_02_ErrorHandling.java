package com.doggybites;

import org.junit.Test;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Step_02_ErrorHandling {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
        @Override
        public synchronized String toString(String charsetName) throws UnsupportedEncodingException {
            String out = super.toString(charsetName);
            System.out.print(out);
            return out;
        }
    };

    private PrintStream out = new PrintStream(outputStream);

    private Observable<Integer> ints = Observable.create(subscriber -> {
        subscriber.onNext(1);
        subscriber.onNext(3);
        subscriber.onError(new RuntimeException("Error"));
    });

    @Test(expected = RuntimeException.class)
    public void throws_when_printing_ints() {
        //TODO
    }

    @Test
    public void works_fine_when_first_two_items_taken() throws UnsupportedEncodingException {
        //TODO

        assertThat(outputStream.toString("UTF-8"), is("1\n3\n"));
    }

    @Test
    public void converts_error_ints_to_minusOne() throws UnsupportedEncodingException {
        //TODO

        assertThat(outputStream.toString("UTF-8"), is("1\n3\n-1\n"));
    }
}
