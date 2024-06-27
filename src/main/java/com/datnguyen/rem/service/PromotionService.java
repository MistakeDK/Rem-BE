package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.PromotionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;

public interface PromotionService {
    void addPromotion(PromotionRequest request);
    PromotionResponse getPromotionByID(String id) throws JsonProcessingException;

    @Transactional
    void ChangePromotionStatus(String promotionCode);
}
