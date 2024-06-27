package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.PromotionResponse;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.PromotionMapper;
import com.datnguyen.rem.repository.PromotionRepository;
import com.datnguyen.rem.service.PromotionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class PromotionServiceImpl implements PromotionService {
    PromotionRepository repository;
    PromotionMapper mapper;
    PromotionRedisServiceImpl promotionRedisService;
    @Transactional
    @Override
    public void addPromotion(PromotionRequest request){
        if(repository.findById(request.getPromotionCode()).isPresent()){
            throw new AppException(ErrorCode.PROMOTION_EXIST);
        }
        var promotion=mapper.toPromotion(request);
        repository.save(promotion);
    }
    @Override
    public PromotionResponse getPromotionByID(String promotionCode) throws JsonProcessingException {
        var promotion=promotionRedisService.getPromotion(promotionCode);
        if(promotion==null){
            promotion=repository.findById(promotionCode).
                    orElseThrow(()->new AppException(ErrorCode.PROMOTION_NOT_EXIST));
            promotionRedisService.savePromotion(promotion);
        }
        if(!promotion.getActive()){
            throw new AppException(ErrorCode.PROMOTION_IS_NOT_ACTIVE);
        }
        return mapper.toPromotionResponse(promotion);
    }
    @Override
    @Transactional
    public void ChangePromotionStatus(String promotionCode){
        var promotion=repository.findById(promotionCode).
                orElseThrow(()->new AppException(ErrorCode.PROMOTION_NOT_EXIST));
        promotion.setActive(!promotion.getActive());
    }
}
