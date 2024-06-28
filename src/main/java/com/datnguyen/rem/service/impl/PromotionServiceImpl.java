package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.PromotionResponse;
import com.datnguyen.rem.entity.Promotion;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.PromotionMapper;
import com.datnguyen.rem.repository.PromotionRepository;
import com.datnguyen.rem.repository.specification.PromotionSpecificationBuilder;
import com.datnguyen.rem.service.PromotionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public PageResponse<?> getList(Pageable pageable, String[] promotion) {
        Page<Promotion> promotions=null;
        if(promotion!=null){
            PromotionSpecificationBuilder builder=new PromotionSpecificationBuilder();
            for (String p:promotion){
                Pattern pattern=Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher=pattern.matcher(p);
                if(matcher.find()){
                    builder.with(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                }
            }
            promotions=repository.findAll(builder.build(),pageable);
        }
        else {
            promotions= repository.findAll(pageable);
        }
        return PageResponse.builder()
                .totalItem(promotions.getTotalElements())
                .totalPage(promotions.getTotalPages())
                .pageSize(pageable.getPageSize())
                .pageNo(pageable.getPageNumber())
                .items(promotions.stream().map(mapper::toPromotionResponse).toList())
                .build();
    }
}
