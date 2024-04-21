package com.example.demo.facade;

import com.example.demo.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockStockFacade {
    private RedissonClient redissonClient;

    private StockService stockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity){
        RLock lock = redissonClient.getLock(id.toString());
        try{
            boolean avalable = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if(!avalable){
                System.out.println("lock 획득 실패");
                return;
            }

            stockService.decrease(id,quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}
