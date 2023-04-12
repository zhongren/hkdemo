package com.example.hk.demo.api.camera;

import com.example.hk.demo.base.ResultDto;
import com.example.hk.demo.api.camera.req.TakeReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "摄像头")
@RestController
public class CameraController {

    @ApiOperation(value = "拍照")
    @PostMapping(value = "camera/take")
    public ResultDto take(@RequestBody TakeReq request) {
        return ResultDto.success();
    }


}
