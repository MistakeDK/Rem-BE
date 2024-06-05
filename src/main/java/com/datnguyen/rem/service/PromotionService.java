package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.PromotionResponse;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.PromotionMapper;
import com.datnguyen.rem.repository.PromotionRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class PromotionService {
    PromotionRepository repository;
    PromotionMapper mapper;
    public void addPromotion(PromotionRequest request){
        if(repository.findById(request.getPromotionCode()).isPresent()){
            throw new AppException(ErrorCode.PROMOTION_EXIST);
        }
        var promotion=mapper.toPromotion(request);
        repository.save(promotion);
    }
    public PromotionResponse getPromotionByID(String id){
        var promotion=repository.findById(id);
        return mapper.toPromotionResponse(repository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.PROMOTION_NOT_EXIST)));
    }
}
