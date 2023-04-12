package com.example.hk.demo.api.txai;

import com.example.hk.demo.base.BaseException;
import com.example.hk.demo.base.BizEnum;
import com.example.hk.demo.base.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "腾讯AI")
@RestController
public class TxAiController {

    @Autowired
    TxAiService txAiService;

    @ApiOperation(value = "提交样本图片")
    @PostMapping(value = "txai/imageDefect")
    public ResultDto imageDefect(@RequestParam(value = "files") MultipartFile files) {
        throw new BaseException(BizEnum.BIZ_ERROR.getCode(), "未获取到上传文件");
       // return ResultDto.success();
    }


}
