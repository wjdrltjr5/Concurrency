package com.example.demo.facade;

import com.example.demo.domain.Stock;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.PessimisticLockStockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class OptimisticLockStockFacadeTest {
    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void before(){
        stockRepository.save(Stock.builder()
                .id(1L)
                .quantity(100L)
                .version(1L)
                .build());
    }

    @AfterEach
    void after(){
        stockRepository.deleteAll();
    }

    @DisplayName("동시에 재고감소 요청이 100개가 온다면")
    @Test
    void decrease100() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //when
        for(int i = 0; i < threadCount; i++){
            executorService.submit(() ->{
                try{
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Stock stock = stockRepository.findById(1L).orElseThrow();
        //then
        //실패하는 테스트 왜와이? -> 레이스 컨디션(경쟁상태 발생)
        assertThat(stock.getQuantity()).isZero();
        System.out.println(stock.getVersion());
    }
}