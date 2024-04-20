package com.example.demo.service;

import com.example.demo.domain.Stock;
import com.example.demo.repository.StockRepository;
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
import static org.assertj.core.api.Assertions.linesOf;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void before(){
        stockRepository.save(Stock.builder().id(1L).quantity(100L).build());
    }

    @AfterEach
    void after(){
        stockRepository.deleteAll();
    }

    @DisplayName("원하는 재고만큼 재고 수를 감소한다.")
    @Test
    void decrease(){
        //given

        //when
        stockService.decrease(1L,1L);
        //then
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertThat(stock.getQuantity()).isEqualTo(99);
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
                    stockService.decrease(1L, 1L);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        Stock stock = stockRepository.findById(1L).orElseThrow();
        //then
        //실패하는 테스트 왜와이? -> 레이스 컨디션(경쟁상태 발생)
        assertThat(stock.getQuantity()).isZero();
    }
}