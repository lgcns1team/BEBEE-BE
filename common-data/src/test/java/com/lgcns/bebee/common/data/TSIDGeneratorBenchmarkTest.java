package com.lgcns.bebee.common.data;

import io.hypersistence.tsid.TSID;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

public class TSIDGeneratorBenchmarkTest {
    private final int THREAD_COUNT = 16;
    private final int IDS_PER_THREAD = 100_000;
    private final int TOTAL_IDS = THREAD_COUNT * IDS_PER_THREAD;
    private final int WARM_UP_COUNT = 100_000;

    private TSID.Factory tsidFactory = TSID.Factory.builder()
            .withNode(1)
            .build();

    @Test
    void TSID_성능_테스트() throws InterruptedException {
        // 워밍업
        for(int i = 0; i < WARM_UP_COUNT; i++){
            tsidFactory.generate().toLong();
        }

        long startTime = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

        for(int i = 0; i < THREAD_COUNT; i++){
            pool.submit(() -> {
                try{
                    for(int j = 0; j < IDS_PER_THREAD; j++){
                        tsidFactory.generate().toLong();
                    }
                }finally{
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();

        long durationMills = System.currentTimeMillis() - startTime;
        double tps = TOTAL_IDS / (durationMills / 1000.0);

        System.out.printf("TSID 생성:  %dms, TPS = %,.2f\n", durationMills, tps);
    }

    @Test
    void TSID_충돌_테스트() throws InterruptedException {
        ConcurrentHashMap.KeySetView<Long, Boolean> generatedIds = ConcurrentHashMap.newKeySet();

        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

        for(int i = 0; i < THREAD_COUNT; i++){
            pool.submit(() -> {
                try{
                    for(int j = 0; j < IDS_PER_THREAD; j++){
                        generatedIds.add(tsidFactory.generate().toLong());
                    }
                }finally{
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();

        if(generatedIds.size() == TOTAL_IDS){
            System.out.printf("TSID 충돌 없음: 기대=%d, 실제=%d\n", TOTAL_IDS, generatedIds.size());
        }else{
            fail("TSID ID 충돌! 기대=%d, 실제=%d\n", TOTAL_IDS, generatedIds.size());
        }
    }

    @Test
    void ObjectId_성능_테스트() throws InterruptedException {
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            new ObjectId().toByteArray();
        }

        long startTime = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            pool.submit(() -> {
                try {
                    for (int j = 0; j < IDS_PER_THREAD; j++) {
                        new ObjectId().toByteArray();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();

        long durationMills = System.currentTimeMillis() - startTime;
        double tps = TOTAL_IDS / (durationMills / 1000.0);

        System.out.printf("ObjectId 생성: %dms, TPS = %,.2f\n", durationMills, tps);
    }
}
