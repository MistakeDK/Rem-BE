package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.CartDetailRequest;
import com.datnguyen.rem.dto.response.CartDetailResponse;
import com.datnguyen.rem.entity.CartDetail;
import com.datnguyen.rem.entity.embedded.CartDetail_ID;
import com.datnguyen.rem.enums.ActionCartQuantity;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.CartDetailMapper;
import com.datnguyen.rem.repository.CartDetailRepository;
import com.datnguyen.rem.repository.ProductRepository;
import com.datnguyen.rem.repository.UserRepository;
import com.datnguyen.rem.service.CartDetailService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CartDetailServiceImpl implements CartDetailService {
    CartDetailRepository cartDetailRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    CartDetailMapper mapper;
    @Override
    public List<CartDetailResponse> getList(String idUser){
        var result= cartDetailRepository.findByCartDetailIdUserId(idUser);
        return result.stream().map(mapper::toCartDetailResponse).toList();
    }
    @Transactional
    @Override
    public void changeQuantity(CartDetailRequest request,String idUser){
        var cartDetail= cartDetailRepository.
                findByCartDetailIdUserIdAndCartDetailIdProductId(idUser,request.getProductId());
        ActionCartQuantity actionCartQuantity=ActionCartQuantity.valueOf(request.getAction());
        switch (actionCartQuantity){
            case  INCREASE-> {
                if(cartDetail!=null){
                    cartDetail.setQuantity(cartDetail.getQuantity()+request.getQuantity());
                }else {
                    CartDetail_ID cartDetailId=CartDetail_ID.builder()
                            .product(productRepository.findById(request.getProductId()).
                                    orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXIST)))
                            .user(userRepository.findById(idUser).
                                    orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)))
                            .build();
                    CartDetail newCartDetail=CartDetail.builder()
                            .cartDetailId(cartDetailId)
                            .quantity(request.getQuantity())
                            .build();
                    cartDetailRepository.save(newCartDetail);
                }
                break;
            }
            case DECREASE -> {
                if(cartDetail==null){
                    return;
                }
                cartDetail.setQuantity(cartDetail.getQuantity()-1);
                if(cartDetail.getQuantity()==0){
                    cartDetailRepository.deleteById(cartDetail.getCartDetailId());
                }
                break;
            }
            case DELETE -> {
                cartDetailRepository.deleteById(cartDetail.getCartDetailId());
                break;
            }
            default -> {
                return;
            }
        }
    }
}
