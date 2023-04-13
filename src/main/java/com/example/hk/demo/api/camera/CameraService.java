package com.example.hk.demo.api.camera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ren.zhong@chebada.com
 * @date: 2023年04月12日13:23:30
 **/
@Service
public class CameraService {

    @Autowired
    CamaraHelper camaraHelper;


    public void takePic(int camIndex){
        //拍照
        camaraHelper.takePicJPEG(camIndex);
        //腾讯处理
        //Base64.encodeBase64String(bytes);
    }
}