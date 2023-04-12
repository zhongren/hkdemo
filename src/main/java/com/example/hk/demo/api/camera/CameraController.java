package com.example.hk.demo.api.camera;

import com.example.hk.demo.base.ResultDto;
import com.example.hk.demo.api.camera.req.TakeReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;

@Api(tags = "摄像头")
@RestController
public class CameraController {

    @Autowired
    CameraService cameraService;

    @ApiOperation(value = "拍照")
    @PostMapping(value = "camera/take")
    public ResultDto take(@RequestBody TakeReq request) {
        String path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(path);
        cameraService.takePic();
        return ResultDto.success();
    }


}
