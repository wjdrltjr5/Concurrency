package com.example.demo.facade;

import com.example.demo.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
// 실패시 재시도하는 클래스
@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while(true){
            try{
                optimisticLockStockService.decrease(id,quantity);
                break;
            }catch (Exception e){
                Thread.sleep(50);
            }
        }
    }
}
