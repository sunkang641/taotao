package com.taotao.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PictureService {
    public Map uploadPicture(MultipartFile uploadFile);
}
