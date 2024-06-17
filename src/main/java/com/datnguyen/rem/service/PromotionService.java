package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.PromotionResponse;

public interface PromotionService {
    void addPromotion(PromotionRequest request);
    PromotionResponse getPromotionByID(String id);
}
