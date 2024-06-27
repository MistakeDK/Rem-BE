package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.entity.Promotion;
import com.datnguyen.rem.service.PromotionRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionRedisServiceImpl implements PromotionRedisService {
    private final BaseRedisServiceImpl baseRedisService;
    private final ObjectMapper redisObjectMapper;
    @Override
    public void savePromotion(Promotion promotion) throws JsonProcessingException {
        String promotionJson=(String) redisObjectMapper.writeValueAsString(promotion);
        baseRedisService.Set(String.format("promotion:%s",promotion.getPromotionCode()),promotionJson);
    }

    @Override
    public Promotion getPromotion(String promotionCode) throws JsonProcessingException {
        String promotionJson= (String) baseRedisService.get(String.format("promotion:%s",promotionCode));
        if(promotionJson==null){
            return null;
        }
        return redisObjectMapper.readValue(promotionJson,Promotion.class);
    }
    @Override
    public void deletePromotionCache(String promotionCode){
        baseRedisService.delete(String.format("promotion:%s",promotionCode));
    }

}
