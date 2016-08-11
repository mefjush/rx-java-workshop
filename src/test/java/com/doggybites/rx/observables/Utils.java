package com.doggybites.rx.observables;

import static rx.Observable.from;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class Utils {

    private Utils() {
        
    }
    
    public static <T> Observable<T> fromList(T... ts) {
        
        return from(ts);
    }
    
    public static <T> Observable<T> fixedDelay(Observable<T> arg, long t, TimeUnit u) {
        
        return Observable.interval(0, t, u).zipWith(arg, (n, p) -> p);   
    }

    public static <T> void dump(Observable<T> arg) {
        
        arg.toBlocking().toIterable().forEach(x -> System.out.println(x));
        
        System.out.println();
    }
    
    public static Observable<Integer> range(int start, int count) {
        
        return Observable.range(start, count);
    }

}
