package com.datnguyen.rem.dto.response;

import com.datnguyen.rem.enums.UserProvide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class StatUserResponse {
    UserProvide userProvide;
    Long count;
}
