package com.datnguyen.rem.entity.eventListener;

import com.datnguyen.rem.entity.Promotion;
import com.datnguyen.rem.service.impl.PromotionRedisServiceImpl;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromotionListener {
    private final PromotionRedisServiceImpl promotionRedisService;
    @PostUpdate
    public void postUpdate(Promotion promotion){
        promotionRedisService.deletePromotionCache(promotion.getPromotionCode());
    }
}
