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
    @Transactional
    public void decrease(Long id, Long quantity){
        //stock 조회
        // 재고 감소
        // 갱신값 저장
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
    }

}
