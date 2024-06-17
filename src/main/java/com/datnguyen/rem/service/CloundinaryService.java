package com.datnguyen.rem.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloundinaryService {
    Map uploadFile(MultipartFile file) throws IOException;
}
