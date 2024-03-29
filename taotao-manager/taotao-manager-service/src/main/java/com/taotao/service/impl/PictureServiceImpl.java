package com.taotao.service.impl;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 图片上传Service
 */
@Service
public class PictureServiceImpl implements PictureService {

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_BASE_PATH}")
    private String FTP_BASE_PATH;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public Map uploadPicture(MultipartFile uploadFile){
        Map resuleMap = new HashMap();
        try {
            //生成一个新的文件名
            //取原始文件名
            String oldName = uploadFile.getOriginalFilename();
            //生成新的文件名
            String newName = IDUtils.genImageName();
            newName = newName + oldName.substring(oldName.lastIndexOf("."));
            //图片上传
            String imagePath = new DateTime().toString("/yyyy/MM/dd");
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH,
                    imagePath, newName, uploadFile.getInputStream());
            //返回结果
            if (!result){
                resuleMap.put("error",1);
                resuleMap.put("message","文件上传失败");
                return resuleMap;
            }
            resuleMap.put("error",0);
            resuleMap.put("url",IMAGE_BASE_URL + imagePath + "/" + newName);
            return resuleMap;
        } catch (IOException e) {
            resuleMap.put("error",1);
            resuleMap.put("message","文件上传发生异常");
            return resuleMap;
        }

    }
}
