package com.doggybites.rx.observables;

import org.junit.Test;

import rx.Observable;
import rx.Observable.Transformer;

import static com.doggybites.rx.observables.Utils.range;
import static com.doggybites.rx.observables.Utils.dump;

public class Transformers {

    public static final Transformer<Integer,Integer> SQUARED = x -> x.map(y -> y*y);
    
    public static final Transformer<Integer,Integer> ODD = x -> x.filter(y -> y%2==1);
    
    
    public static final Transformer<Integer,Integer> DIFF_ATTEMPT = x -> x.skip(1).zipWith(x, (u,v)->u-v);
    
    public static final Transformer<Integer,Integer> DIFF = x -> x.zipWith(x.startWith(0), (u,v)->u-v);
    
    
    public static final Transformer<Integer,Integer> INTEGRAL = x -> x.scan(0, (y,z) -> y+z).skip(1);
    
    
    public static final Transformer<Integer,Integer> HIGHER_ORDER = x -> x.compose(DIFF).compose(INTEGRAL);
    
    
    @Test
    public void basicMap() {
        Observable<Integer> squares = range(1,6).map(y -> y*y);
        
        dump(squares);
    }
    
    @Test
    public void basicReduce() {
        Observable<Integer> sum = range(1,10).reduce((x,y) -> x+y);
        
        dump(sum);
    }

    
    @Test
    public void compose() {
        Observable<Integer> squares = range(1,6).compose(SQUARED);
                
        Observable<Integer> squaresDelta =  squares.compose(DIFF);
        
        dump(squares);
        
        dump(squaresDelta);
    }
    
    @Test
    public void compose2() {
        Observable<Integer> odd = range(1,12).compose(ODD);
                
        Observable<Integer> integrated =  odd.compose(INTEGRAL);
        
        dump(odd);

        dump(integrated);
    }
    
    @Test
    public void compose3() {
        Observable<Integer> check = range(1,5).compose(HIGHER_ORDER);
                
        dump(check);
    }

}
