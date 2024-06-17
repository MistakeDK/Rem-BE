package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.ReviewRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.entity.OrderDetail;
import com.datnguyen.rem.entity.Review;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.ReviewMapper;
import com.datnguyen.rem.repository.OrderDetailRepository;
import com.datnguyen.rem.repository.ReviewRepository;
import com.datnguyen.rem.service.ReviewService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository repository;
    OrderDetailRepository orderDetailRepository;
    ReviewMapper mapper;

    @Transactional
    @Override
    public void createReview(ReviewRequest request){
        OrderDetail orderDetail= orderDetailRepository
                .findByProduct_IdAndOrder_Id(request.getProductId(), request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED_IN_ORDER));
        if(orderDetail.getIsReview()){
            throw new AppException(ErrorCode.REVIEW_OF_PRODUCT_AND_ORDER_EXISTED);
        }
        Review review=mapper.toReview(request);
        repository.save(review);
        orderDetail.setIsReview(true);
    }
    @Override
    public PageResponse<?> getList(String idProduct, int pageNo, int pageSize){
        if(pageNo>0){
            pageNo=pageNo-1;
        }
        Sort.Order sort=new Sort.Order(Sort.Direction.DESC,"timeCreate");
        Pageable pageable= PageRequest.of(pageNo,pageSize,Sort.by(sort));
        var listReview= repository.findById_Product_Id(idProduct,pageable);
        return  PageResponse.builder()
                .totalItem(listReview.getTotalElements())
                .items(listReview.stream().map(mapper::toReviewResponse).toList())
                .pageSize(pageSize)
                .totalPage(listReview.getTotalPages())
                .pageNo(pageNo+1)
                .build();
    }
}
