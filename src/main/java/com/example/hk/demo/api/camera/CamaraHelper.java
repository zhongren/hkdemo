package com.example.hk.demo.api.camera;

import MvCameraControlWrapper.CameraControlException;
import MvCameraControlWrapper.CameraImageCallBack;
import MvCameraControlWrapper.MvCameraControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static MvCameraControlWrapper.MvCameraControl.MV_CC_EnumDevices;
import static MvCameraControlWrapper.MvCameraControlDefines.*;

/**
 * 摄像头辅助类
 */
@Slf4j
@Component
public class CamaraHelper {

    public static Handle hCamera = null;

    private static void printDeviceInfo(MV_CC_DEVICE_INFO stDeviceInfo) {
        if (null == stDeviceInfo) {
            log.info("stDeviceInfo is null");
            return;
        }

        if (stDeviceInfo.transportLayerType == MV_GIGE_DEVICE) {
            log.info("\tCurrentIp:       " + stDeviceInfo.gigEInfo.currentIp);
            log.info("\tModel:           " + stDeviceInfo.gigEInfo.modelName);
            log.info("\tUserDefinedName: " + stDeviceInfo.gigEInfo.userDefinedName);
        } else if (stDeviceInfo.transportLayerType == MV_USB_DEVICE) {
            log.info("\tUserDefinedName: " + stDeviceInfo.usb3VInfo.userDefinedName);
            log.info("\tSerial Number:   " + stDeviceInfo.usb3VInfo.serialNumber);
            log.info("\tDevice Number:   " + stDeviceInfo.usb3VInfo.deviceNumber);
        } else {
            System.err.print("Device is not supported! \n");
        }

        log.info("\tAccessible:      "
                + MvCameraControl.MV_CC_IsDeviceAccessible(stDeviceInfo, MV_ACCESS_Exclusive));
        log.info("");
    }

    private static void printFrameInfo(MV_FRAME_OUT_INFO stFrameInfo) {
        if (null == stFrameInfo) {
            System.err.println("stFrameInfo is null");
            return;
        }

        StringBuilder frameInfo = new StringBuilder("");
        frameInfo.append(("\tFrameNum[" + stFrameInfo.frameNum + "]"));
        frameInfo.append("\tWidth[" + stFrameInfo.width + "]");
        frameInfo.append("\tHeight[" + stFrameInfo.height + "]");
        frameInfo.append(String.format("\tPixelType[%#x]", stFrameInfo.pixelType.getnValue()));

        log.info(frameInfo.toString());
    }

    public static int chooseCamera(ArrayList<MV_CC_DEVICE_INFO> stDeviceList) {
        if (null == stDeviceList) {
            return -1;
        }
        // Choose a device to operate
        int camIndex = -1;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Please input camera index (-1 to quit):");
                camIndex = scanner.nextInt();
                if ((camIndex >= 0 && camIndex < stDeviceList.size()) || -1 == camIndex) {
                    break;
                } else {
                    log.info("Input error: " + camIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
                camIndex = -1;
                break;
            }
        }
        scanner.close();

        if (-1 == camIndex) {
            log.info("Bye.");
            return camIndex;
        }

        if (0 <= camIndex && stDeviceList.size() > camIndex) {
            if (MV_GIGE_DEVICE == stDeviceList.get(camIndex).transportLayerType) {
                log.info("Connect to camera[" + camIndex + "]: " + stDeviceList.get(camIndex).gigEInfo.userDefinedName);
            } else if (MV_USB_DEVICE == stDeviceList.get(camIndex).transportLayerType) {
                log.info("Connect to camera[" + camIndex + "]: " + stDeviceList.get(camIndex).usb3VInfo.userDefinedName);
            } else {
                log.info("Device is not supported.");
            }
        } else {
            log.info("Invalid index " + camIndex);
            camIndex = -1;
        }

        return camIndex;
    }



    @Async("cameraExecutor")
    public void takePicJPEG(){
        int nRet = MV_OK;
        int camIndex = -1;
        ArrayList<MV_CC_DEVICE_INFO> stDeviceList = null;
        do {
            log.info("SDK Version " + MvCameraControl.MV_CC_GetSDKVersion());

            // Enumerate GigE and USB devices
            try {
                stDeviceList = MvCameraControl.MV_CC_EnumDevices(MV_GIGE_DEVICE | MV_USB_DEVICE);
                if (0 >= stDeviceList.size()) {
                    log.info("No devices found!");
                    break;
                }
                int i = 0;
                for (MV_CC_DEVICE_INFO stDeviceInfo : stDeviceList) {
                    if (null == stDeviceInfo) {
                        continue;
                    }
                    log.info("[camera " + (i++) + "]");
                    printDeviceInfo(stDeviceInfo);
                }
            } catch (CameraControlException e) {
                System.err.println("Enumrate devices failed!" + e.toString());
                e.printStackTrace();
                break;
            }

            // choose camera
            camIndex = chooseCamera(stDeviceList);
            if (-1 == camIndex) {
                break;
            }

            // Create device handle
            try {
                hCamera = MvCameraControl.MV_CC_CreateHandle(stDeviceList.get(camIndex));
            } catch (CameraControlException e) {
                System.err.println("Create handle failed!" + e.toString());
                e.printStackTrace();
                hCamera = null;
                break;
            }

            // Open selected device
            nRet = MvCameraControl.MV_CC_OpenDevice(hCamera);
            if (MV_OK != nRet) {
                System.err.printf("Connect to camera failed, errcode: [%#x]\n", nRet);
                break;
            }


            // set continuous acquisition
            nRet = MvCameraControl.MV_CC_SetEnumValueByString(hCamera, "AcquisitionMode", "Continuous");
            if (MV_OK != nRet) {
                System.err.printf("Set AcquisitionMode to Continous failed, errcode: [%#x]\n", nRet);
                break;
            }

            // Turn on trigger mode
            nRet = MvCameraControl.MV_CC_SetEnumValueByString(hCamera, "TriggerMode", "On");
            if (MV_OK != nRet) {
                System.err.printf("SetTriggerMode failed, errcode: [%#x]\n", nRet);
                break;
            }

            // set trigger source to software
            nRet = MvCameraControl.MV_CC_SetEnumValueByString(hCamera, "TriggerSource", "Software");
            if (MV_OK != nRet) {
                System.err.printf("SetTriggerSource to software failed, errcode: [%#x]\n", nRet);
                break;
            }


            // Register image callback
            nRet = MvCameraControl.MV_CC_RegisterImageCallBack(hCamera, new CameraImageCallBack() {
                @Override
                public int OnImageCallBack(byte[] bytes, MV_FRAME_OUT_INFO mv_frame_out_info) {
                    printFrameInfo(mv_frame_out_info);
                    // Get payload size
                    MVCC_INTVALUE stParam = new MVCC_INTVALUE();
                    MvCameraControl.MV_CC_GetIntValue(hCamera, "PayloadSize", stParam);

                    byte[] pData = new byte[(int) stParam.curValue];
                    MV_FRAME_OUT_INFO stImageInfo = mv_frame_out_info;
                    int imageLen = stImageInfo.width * stImageInfo.height * 3;    // Every RGB pixel takes 3 bytes
                    byte[] imageBuffer = new byte[imageLen];

                    // Call MV_CC_SaveImage to save image as JPEG
                    MV_SAVE_IMAGE_PARAM stSaveParam = new MV_SAVE_IMAGE_PARAM();
                    stSaveParam.width = stImageInfo.width;                                  // image width
                    stSaveParam.height = stImageInfo.height;                                // image height
                    stSaveParam.data = bytes;                                               // image data
                    stSaveParam.dataLen = stImageInfo.frameLen;                             // image data length
                    stSaveParam.pixelType = stImageInfo.pixelType;                          // image pixel format
                    stSaveParam.imageBuffer = imageBuffer;                                  // output image buffer
                    stSaveParam.imageType = MV_SAVE_IAMGE_TYPE.MV_Image_Jpeg;               // output image pixel format
                    stSaveParam.methodValue = 0;                                            // Interpolation method that converts Bayer format to RGB24.  0-Neareast 1-double linear 2-Hamilton
                    stSaveParam.jpgQuality = 60;                                            // JPG endoding quality(50-99]

                    MvCameraControl.MV_CC_SaveImage(hCamera, stSaveParam);
                    SaveImage.saveDataToFile(imageBuffer, stSaveParam.imageLen, System.currentTimeMillis() + ".jpeg");
                    return 0;
                }
            });
            if (MV_OK != nRet) {
                System.err.printf("register image callback failed, errcode: [%#x]\n", nRet);
                break;
            }

            // Start grabbing
            nRet = MvCameraControl.MV_CC_StartGrabbing(hCamera);
            if (MV_OK != nRet) {
                System.err.printf("StartGrabbing failed, errcode: [%#x]\n", nRet);
                break;
            }

            log.info("Software trigger: ");
            for (int i = 0; i < 10; i++) {
                nRet = MvCameraControl.MV_CC_SetCommandValue(hCamera, "TriggerSoftware");
                if (MV_OK != nRet) {
                    System.err.printf("Software trigger failed, errcode: [%#x]\n", nRet);
                }

                try {
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Turn off trigger mode and stop acquisition
            nRet = MvCameraControl.MV_CC_SetEnumValueByString(hCamera, "TriggerMode", "Off");
            if (MV_OK != nRet) {
                System.err.printf("SetTriggerMode failed, errcode: [%#x]\n", nRet);
                break;
            }
            nRet = MvCameraControl.MV_CC_StopGrabbing(hCamera);
            if (MV_OK != nRet) {
                System.err.printf("StopGrabbing failed, errcode: [%#x]\n", nRet);
                break;
            }

        } while (false);

        if (null != hCamera) {
            // Destroy handle
            nRet = MvCameraControl.MV_CC_DestroyHandle(hCamera);
            if (MV_OK != nRet) {
                System.err.printf("DestroyHandle failed, errcode: [%#x]\n", nRet);
            }
        }
    }


}
