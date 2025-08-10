package com.nicc.ReactiveTutorial.TutorialUtils;

import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BackpressureTutorial {
    private Flux<Long> createNoOverflowFlux() {
        return Flux.range(1, Integer.MAX_VALUE)
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
        // simulate a subscriber that takes time to process each item
    }

    private Flux<Long> createOverflowFlux() {
/*
        When this method is executed without backpressure, we will see the following output:

        15:41:06.884 [main] INFO reactor.Flux.Interval.1 -- onSubscribe(FluxInterval.IntervalRunnable)
        15:41:06.888 [main] INFO reactor.Flux.Interval.1 -- request(1)
        15:41:06.890 [parallel-1] INFO reactor.Flux.Interval.1 -- onNext(0)
        15:41:06.893 [parallel-1] ERROR reactor.Flux.Interval.1 -- onError(reactor.core.Exceptions$OverflowException: Could not emit tick 1 due to lack of requests (interval doesn't support small downstream requests that replenish slower than the ticks))
        15:41:06.893 [parallel-1] ERROR reactor.Flux.Interval.1 --
        reactor.core.Exceptions$OverflowException: Could not emit tick 1 due to lack of requests (interval doesn't support small downstream requests that replenish slower than the ticks)
        at reactor.core.Exceptions.failWithOverflow(Exceptions.java:251)
        at reactor.core.publisher.FluxInterval$IntervalRunnable.run(FluxInterval.java:131)
        at reactor.core.scheduler.PeriodicWorkerTask.call(PeriodicWorkerTask.java:59)
        at reactor.core.scheduler.PeriodicWorkerTask.run(PeriodicWorkerTask.java:73)
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
        at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:358)
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.lang.Thread.run(Thread.java:1583)
        Exception in thread "main" reactor.core.Exceptions$OverflowException: Could not emit tick 1 due to lack of requests (interval doesn't support small downstream requests that replenish slower than the ticks)
        at reactor.core.Exceptions.failWithOverflow(Exceptions.java:251)
        at reactor.core.publisher.FluxInterval$IntervalRunnable.run(FluxInterval.java:131)
        at reactor.core.scheduler.PeriodicWorkerTask.call(PeriodicWorkerTask.java:59)
        at reactor.core.scheduler.PeriodicWorkerTask.run(PeriodicWorkerTask.java:73)
        at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
        at java.base/java.util.concurrent.FutureTask.runAndReset(FutureTask.java:358)
        at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:305)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.lang.Thread.run(Thread.java:1583)
        Suppressed: java.lang.Exception: #block terminated with an error
        at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:104)
        at reactor.core.publisher.Flux.blockLast(Flux.java:2817)
        at com.nicc.ReactiveTutorial.TutorialUtils.BackpressureTutorial.main(BackpressureTutorial.java:27)

        Execution failed for task ':com.nicc.ReactiveTutorial.TutorialUtils.BackpressureTutorial.main()'.

        This output occurs because the Flux emits elements at a rate of 1 element per millisecond, while the subscriber
        only requests 1 element at a time and takes 100 ms to process each element.
*/

        return Flux.interval(Duration.ofMillis(1))
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }

    private Flux<Long> createDropOnBackpressureFlux() {
/*
        When this method is called, we will see the following output:

        Element processed by subscriber: 0
        Element processed by subscriber: 102
        Element processed by subscriber: 205
        Element processed by subscriber: 306
        Element processed by subscriber: 409
        Element processed by subscriber: 511
        ...

        This is obviously not ideal since we are dropping elements, and there would be no way for the subscriber to
        recover the values that were dropped.
*/
        return Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(x -> System.out.println("Element processed by subscriber: " + x));
    }

    private Flux<Long> createBufferTooSmallOnBackpressureFlux() {
/*
        When this method is called, we will see the following output:

        > Task :com.nicc.ReactiveTutorial.TutorialUtils.BackpressureTutorial.main()
        Element processed by subscriber: 0
        Element processed by subscriber: 1
        Element processed by subscriber: 2
        Element processed by subscriber: 3
        Element processed by subscriber: 4
        Element processed by subscriber: 5
        Element processed by subscriber: 6
        Element processed by subscriber: 7
        Element processed by subscriber: 8
        Element processed by subscriber: 9
        Element processed by subscriber: 10
        Element processed by subscriber: 11
        Element processed by subscriber: 12
        Element processed by subscriber: 13
        Element processed by subscriber: 14
        Element processed by subscriber: 15
        Element processed by subscriber: 16
        Element processed by subscriber: 17
        Element processed by subscriber: 18
        Element processed by subscriber: 19
        Element processed by subscriber: 20
        Element processed by subscriber: 21
        Element processed by subscriber: 22
        Element processed by subscriber: 23
        Element processed by subscriber: 24
        Element processed by subscriber: 25
        Element processed by subscriber: 26
        Element processed by subscriber: 27
        Element processed by subscriber: 28
        Element processed by subscriber: 29
        Element processed by subscriber: 30
        Element processed by subscriber: 31
        Element processed by subscriber: 32
        Element processed by subscriber: 33
        Element processed by subscriber: 34
        Element processed by subscriber: 35
        Element processed by subscriber: 36
        Element processed by subscriber: 37
        Element processed by subscriber: 38
        Element processed by subscriber: 39
        Element processed by subscriber: 40
        Element processed by subscriber: 41
        Element processed by subscriber: 42
        Element processed by subscriber: 43
        Element processed by subscriber: 44
        Element processed by subscriber: 45
        Element processed by subscriber: 46
        Element processed by subscriber: 47
        Element processed by subscriber: 48
        Element processed by subscriber: 49
        Exception in thread "main" reactor.core.Exceptions$OverflowException: The receiver is overrun by more signals
        than expected (bounded queue...)....

        In the above method, we see that around 100 elements are emitted between successful elements processed by the
        subscriber. The buffer is only set to 50 here, so when the buffer is full, the program falls over.
*/

        // if a consumer is unable to process the request, elements will go into the buffer
        return Flux.interval(Duration.ofMillis(1))
                .onBackpressureBuffer(50)
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(x -> System.out.println("Element processed by subscriber: " + x));
    }

    private Flux<Long> createBufferDropLatestOnBackpressureFlux() {
/*
        When this method is called, we will see the following output:

        > Task :com.nicc.ReactiveTutorial.TutorialUtils.BackpressureTutorial.main()
        Element processed by subscriber: 0
        Element processed by subscriber: 1
        Element processed by subscriber: 2
        Element processed by subscriber: 3
        Element processed by subscriber: 4
        Element processed by subscriber: 5
        Element processed by subscriber: 6
        Element processed by subscriber: 7
        Element processed by subscriber: 8
        Element processed by subscriber: 9
        Element processed by subscriber: 10
        Element processed by subscriber: 11
        Element processed by subscriber: 12
        Element processed by subscriber: 13
        Element processed by subscriber: 14
        Element processed by subscriber: 15
        Element processed by subscriber: 16
        Element processed by subscriber: 17
        Element processed by subscriber: 18
        Element processed by subscriber: 19
        Element processed by subscriber: 20
        Element processed by subscriber: 21
        Element processed by subscriber: 22
        Element processed by subscriber: 23
        Element processed by subscriber: 24
        Element processed by subscriber: 25
        Element processed by subscriber: 26
        Element processed by subscriber: 27
        Element processed by subscriber: 28
        Element processed by subscriber: 29
        Element processed by subscriber: 30
        Element processed by subscriber: 31
        Element processed by subscriber: 32
        Element processed by subscriber: 33
        Element processed by subscriber: 34
        Element processed by subscriber: 35
        Element processed by subscriber: 36
        Element processed by subscriber: 37
        Element processed by subscriber: 38
        Element processed by subscriber: 39
        Element processed by subscriber: 40
        Element processed by subscriber: 41
        Element processed by subscriber: 42
        Element processed by subscriber: 43
        Element processed by subscriber: 44
        Element processed by subscriber: 45
        Element processed by subscriber: 46
        Element processed by subscriber: 47
        Element processed by subscriber: 48
        Element processed by subscriber: 49
        Element processed by subscriber: 50
        Element processed by subscriber: 104
        Element processed by subscriber: 205
        Element processed by subscriber: 310
        Element processed by subscriber: 411
        Element processed by subscriber: 515
        ...

        The latest element is dropped when the buffer is full, and the subscriber continues to process elements. We can
        see that after the subscriber processes element 50, the next element processed is 104, meaning that everything
        between 50 and 104 was dropped.
*/

        return Flux.interval(Duration.ofMillis(1))
                .onBackpressureBuffer(50, BufferOverflowStrategy.DROP_LATEST) // drop the latest element when the buffer is full
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(x -> System.out.println("Element processed by subscriber: " + x));
    }

    public static void main(String[] args) {
        BackpressureTutorial tutorial = new BackpressureTutorial();
//        tutorial.createNoOverflowFlux()
//                .blockLast(); // block until flux is complete; automatically subscribes since it is a blocking call
//        tutorial.createOverflowFlux()
//                .blockLast();
//        tutorial.createDropOnBackpressureFlux()
//                .blockLast();
//        tutorial.createBufferTooSmallOnBackpressureFlux()
//                .blockLast();
        tutorial.createBufferDropLatestOnBackpressureFlux()
                .blockLast();
    }
}
