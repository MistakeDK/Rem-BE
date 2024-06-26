package com.datnguyen.rem.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.datnguyen.rem.service.CloundinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CloundinaryServiceImpl implements CloundinaryService {
    Cloudinary cloudinary;
    public Map uploadFile(MultipartFile file)
            throws IOException {
        Map data=cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return data;
    }
}
