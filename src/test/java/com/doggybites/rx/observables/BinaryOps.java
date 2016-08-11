package com.doggybites.rx.observables;

import static com.doggybites.rx.observables.Utils.dump;
import static com.doggybites.rx.observables.Utils.fixedDelay;
import static com.doggybites.rx.observables.Utils.fromList;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import rx.Observable;

public class BinaryOps {

    @Test
    public void display() {
        Observable<Integer> numbers = fromList(1,2,3,2,3);
        
        dump(numbers);
    }
    
    @Test
    public void timing() {
        Observable<Integer> numbers = fixedDelay(fromList(1,2,3,2,3), 1L, TimeUnit.SECONDS);
        
        dump(numbers);
    }

    @Test
    public void concat() {
        Observable<Integer> series1 = fromList(1,2,3);
        Observable<Integer> series2 = fromList(4,5,6);
        
        Observable<Integer> series = series1.concatWith(series2);
        
        dump(series);
    }
            
    @Test
    public void merge() {
        Observable<Integer> series1 = fixedDelay(fromList(0,1,2,3,4,5,6,7,8,9),200L,TimeUnit.MILLISECONDS).delay(50L, TimeUnit.MILLISECONDS);
        Observable<Integer> series2 = fixedDelay(fromList(10,20,30,40,50),400L,TimeUnit.MILLISECONDS);
        
        Observable<Integer> merged = series1.mergeWith(series2);
        
        dump(merged);
    }
    
    @Test
    public void binaryOperation() {
        Observable<Integer> series1 = fixedDelay(fromList(0,1,2,3,4,5,6,7,8,9),200L,TimeUnit.MILLISECONDS).delay(50L, TimeUnit.MILLISECONDS);
        Observable<Integer> series2 = fixedDelay(fromList(10,20,30,40,50),400L,TimeUnit.MILLISECONDS);
        
        Observable<Integer> sum = Observable.combineLatest(series1, series2, (x,y) ->x+y);
        
        dump(sum);
    }
    
    @Test
    public void equality() {
        Observable<Integer> series1 = fromList(1,2,3);
        Observable<Integer> series2 = fromList(1,2,3);
        
        Observable<Boolean> equal = series1.zipWith(series2, (x,y) -> x==y).reduce((x,y) -> x && y);

        dump(equal);
    }

}
