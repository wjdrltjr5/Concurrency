package com.example.demo.service;

import com.example.demo.domain.Stock;
import com.example.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PessimisticLockStockService {
    private final StockRepository stockRepository;
    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        stock.decrease(quantity);
    }
}
