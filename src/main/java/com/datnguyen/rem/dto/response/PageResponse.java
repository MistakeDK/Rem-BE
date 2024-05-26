package com.datnguyen.rem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    int pageNo;
    int pageSize;
    long totalItem;
    int totalPage;
    T items;
}
