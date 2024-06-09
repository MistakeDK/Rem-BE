package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.dto.response.OrderResponse;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.ProductResponse;
import com.datnguyen.rem.entity.Order;
import com.datnguyen.rem.mapper.OrderMapper;
import com.datnguyen.rem.repository.CartDetailRepository;
import com.datnguyen.rem.repository.OrderDetailRepository;
import com.datnguyen.rem.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    CartDetailRepository cartDetailRepository;
    OrderMapper orderMapper;
    @Transactional
    public void createOrder(OrderRequest request){
        Order order=orderMapper.toOrder(request);
        var listCart=cartDetailRepository.findByCartDetailIdUserId(request.getUserId());
        var listOrder=orderMapper.toListOrderDetail(listCart);
        listOrder.forEach(orderDetail -> orderDetail.setOrder(order));
        order.setOrderDetails(listOrder);
        if(order.getPromotion().getPromotionCode()==null){
            order.setPromotion(null);
        }
        cartDetailRepository.deleteByCartDetailId_User_Id(request.getUserId());
        orderRepository.save(order);
    }
    public PageResponse<?> getAllOrderByIdUser(String id,int pageNo,int pageSize){
        if(pageNo>0){
            pageNo=pageNo-1;
        }
        Sort.Order sort=new Sort.Order(Sort.Direction.DESC,"timeCreate");
        Pageable pageable= PageRequest.of(pageNo,pageSize,Sort.by(sort));
        var listOrder= orderRepository.findByUser_Id(id,pageable);
        return PageResponse.builder()
                .pageNo(pageNo==0?pageNo+1:pageNo)
                .pageSize(pageSize)
                .totalItem(listOrder.getTotalElements())
                .items(listOrder.stream().map(orderMapper::toOrderResponse))
                .totalPage(listOrder.getTotalPages())
                .build();
    }
}
