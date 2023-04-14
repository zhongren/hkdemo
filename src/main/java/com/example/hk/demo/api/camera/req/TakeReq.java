package com.example.hk.demo.api.camera.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TakeReq {

    //摄像头下标
    @ApiModelProperty(value = "摄像头编号",required = true)
    @NotBlank(message = "请选择摄像头")
    private String camIndex;
}
