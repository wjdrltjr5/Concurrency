package com.example.demo.service;

import com.example.demo.domain.Stock;
import com.example.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    /*@Transactional 메소드에 synchronized를 달면 프록시 객체를
    * 이용하기 때문에 시간차가 발생 synchronized로 완벽하게 해결할 수 없음
    * 아니면 그냥 @Transactional을 없애고 synchronized사용
    * */
/*    @Transactional
    public void decrease(Long id, Long quantity){
        //stock 조회
        // 재고 감소
        // 갱신값 저장
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
    }*/
    public synchronized void decrease(Long id, Long quantity){
        //stock 조회
        // 재고 감소
        // 갱신값 저장
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
