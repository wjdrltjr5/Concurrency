package com.example.demo.facade;

import com.example.demo.repository.RedisLockRepository;
import com.example.demo.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {
    private RedisLockRepository redisLockRepository;
    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(id)){
            Thread.sleep(100);
        }
        try{
            stockService.decrease(id,quantity);
        }finally {
            redisLockRepository.unLock(id);
        }
    }
}
