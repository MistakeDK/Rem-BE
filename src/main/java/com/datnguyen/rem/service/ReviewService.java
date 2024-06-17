package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.ReviewRequest;
import com.datnguyen.rem.dto.response.PageResponse;

public interface ReviewService {
    void createReview(ReviewRequest request);
    PageResponse<?> getList(String idProduct, int pageNo, int pageSize);
}
