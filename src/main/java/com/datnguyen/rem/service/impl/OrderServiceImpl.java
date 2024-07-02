package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.dto.response.OrderResponse;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.entity.Order;
import com.datnguyen.rem.enums.OrderStatus;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.OrderMapper;
import com.datnguyen.rem.repository.CartDetailRepository;
import com.datnguyen.rem.repository.OrderDetailRepository;
import com.datnguyen.rem.repository.OrderRepository;
import com.datnguyen.rem.repository.PromotionRepository;
import com.datnguyen.rem.repository.specification.GenericSpecificationBuilder;
import com.datnguyen.rem.service.OrderService;
import com.datnguyen.rem.utils.SpecificationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    CartDetailRepository cartDetailRepository;
    PromotionRepository promotionRepository;
    OrderMapper orderMapper;
    @Transactional
    @Override
    public String createOrder(OrderRequest request){
        Order order=orderMapper.toOrder(request,promotionRepository);
        var listCart=cartDetailRepository.findByCartDetailIdUserId(request.getUserId());
        var listOrder=orderMapper.toListOrderDetail(listCart);
        listOrder.forEach(orderDetail -> orderDetail.setOrder(order));
        order.setOrderDetails(listOrder);
        order.setTotal(calculateTotal(order));
        cartDetailRepository.deleteByCartDetailId_User_Id(request.getUserId());
        orderRepository.save(order);
        return order.getId();
    }
    @Transactional
    @Override
    public void paidOrderById(String id){
        Order order=orderRepository.findById(id).orElseThrow();
        order.setIsPaid(true);
    }
    @Override
    public PageResponse<?> getAllOrderByIdUser(String id,int pageNo,int pageSize){
        if(pageNo>0){
            pageNo=pageNo-1;
        }
        Sort.Order sort=new Sort.Order(Sort.Direction.DESC,"timeCreate");
        Pageable pageable= PageRequest.of(pageNo,pageSize,Sort.by(sort));
        var listOrder= orderRepository.findByUser_Id(id,pageable);
        return PageResponse.builder()
                .pageNo(pageNo+1)
                .pageSize(pageSize)
                .totalItem(listOrder.getTotalElements())
                .items(listOrder.stream().map(orderMapper::toOrderResponse).toList())
                .totalPage(listOrder.getTotalPages())
                .build();
    }

    @Override
    public PageResponse<?> getList(Pageable pageable, String[] order) {
        Page<Order> orderPage=null;
        if(order!=null){
            GenericSpecificationBuilder<Order> builder=new GenericSpecificationBuilder<>();
            SpecificationUtils.ConvertFormStringToSpecification(builder,order);
            orderPage=orderRepository.findAll(builder.build(Order.class),pageable);
        }else {
            orderPage=orderRepository.findAll(pageable);
        }
        return PageResponse.builder()
                .totalItem(orderPage.getTotalElements())
                .totalPage(orderPage.getTotalPages())
                .pageNo(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .items(orderPage.map(orderMapper::toOrderResponseForAdmin).toList())
                .build();
    }

    @Override
    public OrderResponse getById(String id) {
        var order=orderRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_EXISTED));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public void changeStatus(String id) {
        var order=orderRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_EXISTED));
        switch (order.getStatus()) {
            case RECEIVED -> order.setStatus(OrderStatus.IN_DELIVERY);
            case IN_DELIVERY -> {
                order.setStatus(OrderStatus.DELIVERED);
                order.setIsPaid(true);
            }
            case DELIVERED -> throw new AppException(ErrorCode.ORDER_NOT_CHANGE_STATUS);
        }
    }

    public static Double calculateTotal(Order order){
        Double total=order.getOrderDetails().stream()
                .map(item->item.getPrice()*item.getQuantity()).reduce((double) 0,(Double::sum));
        if(order.getPromotion()==null){
            return total;
        }
        switch (order.getPromotion().getType()){
            case DIRECT -> {
                total-=order.getPromotion().getValue();
            }
            case PERCENT -> {
                double value=1-(order.getPromotion().getValue() / 100);
                total*=value;
            }
        }
        return (double) Math.round(total);

    }
}
