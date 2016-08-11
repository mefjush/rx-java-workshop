package com.doggybites.rx.observables;

import static com.doggybites.rx.observables.Utils.dump;
import static com.doggybites.rx.observables.Utils.fixedDelay;
import static com.doggybites.rx.observables.Utils.range;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import rx.Observable;
import rx.Observable.Transformer;

public class Filter {

    public static final Transformer<Integer,Integer> IS_SQUARE = x -> x.filter(y -> Math.round(Math.sqrt(y))==Math.sqrt(y));

    
    public static final Transformer<Integer,Integer> TIMED = x -> fixedDelay(x, 50, TimeUnit.MILLISECONDS);
        
    public static final Transformer<Integer,Integer> TIME_OUT = x -> x.timeout(500, TimeUnit.MILLISECONDS);

    
    @Test
    public void filter() {
        Observable<Integer> series = range(1,100);
        Observable<Integer> square = series.compose(IS_SQUARE);
        
        dump(square);
    }
    
    @Test
    public void increasingDelay() {
        Observable<Integer> series = range(1,10);
        Observable<Integer> beat = range(1,100).compose(TIMED).compose(IS_SQUARE);
        
        dump(series.zipWith(beat, (x,y) -> x));
        
        dump(series.zipWith(beat, (x,y) -> x).compose(TIME_OUT).onExceptionResumeNext(Observable.empty()));
    }
    
    @Test
    public void echo() {
        
        Random random = new Random();
        
        Observable<Integer> series = range(1,10).compose(TIMED);
        
        dump(series.flatMap(x -> (random.nextInt()%5==0)?
                Observable.just(x,x).compose(TIMED):
                Observable.just(x)
        ));
    }
    
    @Test
    public void hiccup() {
        
        Random random = new Random();
        
        Observable<Integer> series = range(1,10).compose(TIMED);
        
        dump(series.concatMap(x -> (random.nextInt()%5==0)?
                Observable.just(x,x).compose(TIMED):
                Observable.just(x)
        ));
    }


}
