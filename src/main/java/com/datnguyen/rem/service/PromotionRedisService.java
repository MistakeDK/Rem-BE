package com.datnguyen.rem.service;

import com.datnguyen.rem.entity.Promotion;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PromotionRedisService {

    void savePromotion(Promotion promotion) throws JsonProcessingException;

    Promotion getPromotion(String promotionCode) throws JsonProcessingException;

    void deletePromotionCache(String promotionCode);
}
