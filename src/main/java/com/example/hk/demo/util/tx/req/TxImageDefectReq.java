package com.example.hk.demo.util.tx.req;

import com.example.hk.demo.util.tx.TxAiReq;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: ren.zhong@chebada.com
 * @date: 2023年04月12日09:46:57
 **/
@Getter
@Setter
public class TxImageDefectReq extends TxAiReq {

    /**
     * job_id : 20210403120516
     * sample_id : S00001
     * pose_id : P01
     * file_names : ["S00001_C01_P01_L0_20201024100008.jpg"]
     * relative_dir : 20211111093056/Orignal/S00001/
     * ext_params : {"color":"Y0","with_keyboard":1,"xxxx1":"value1","xxxx2":"value2","xxxxN":"valueN"}
     */

    @JsonProperty("job_id")
    private String jobId;
    @JsonProperty("sample_id")
    private String sampleId;
    @JsonProperty("pose_id")
    private String poseId;
    @JsonProperty("file_names")
    private List<String> fileNames;
    @JsonProperty("images_base64")
    private List<String> imagesBase64;

    @JsonProperty("relative_dir")
    private String relativeDir;

    @JsonProperty("ext_params")
    private ExtParamsDTO extParams;


    public static class ExtParamsDTO implements Serializable {
        /**
         * color : Y0
         * with_keyboard : 1
         * xxxx1 : value1
         * xxxx2 : value2
         * xxxxN : valueN
         */

        @JsonProperty("color")
        private String color;
        @JsonProperty("with_keyboard")
        private int withKeyboard;


        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getWithKeyboard() {
            return withKeyboard;
        }

        public void setWithKeyboard(int withKeyboard) {
            this.withKeyboard = withKeyboard;
        }

    }
}