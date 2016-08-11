package com.doggybites.rx.observables;

import org.junit.Test;

import rx.Observable;
import rx.Observable.Transformer;

import static com.doggybites.rx.observables.Utils.dump;
import static com.doggybites.rx.observables.Utils.range;

public class State {

    public static class Stats {
        
        private final int count;
        private final long sum;
        
        public Stats() {
            this(0,0L);
        }
        
        private Stats(int count, long sum) {
            this.count = count;
            this.sum = sum;
        }
        
        private Stats add(int sample) {
            return new Stats(count+1, sum+sample);
        }
        
        private float runningAverage() {
            return (float) 1.0 * sum / count;
        }
    }
    
    public static final Transformer<Integer,Float> RUNNING_AVERAGE = x -> x.scan(new Stats(), (u,v) -> u.add(v)).skip(1).map(w -> w.runningAverage());
    
    @Test
    public void average() {
        
        Observable<Integer> samples = range(1,10);

        Observable<Float> runningAverage = samples.compose(RUNNING_AVERAGE);
        
        dump(runningAverage);
    }

}
