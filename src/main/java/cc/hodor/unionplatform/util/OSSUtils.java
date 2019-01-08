package cc.hodor.unionplatform.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *  Header Name: WellJoint
 *
 *  Description: 
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/7-17:47
 *
 ****************************************************************************************/
@Slf4j
public class OSSUtils {

    public static boolean putFiles(String endpoint, String accessKeyId, String accessKeySecret,
                                   String bucketName, String prefix, List<String> filePathList) {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        for (String filePath:filePathList) {
            String[] fileAttrs = filePath.split("/");
            String objectName = fileAttrs[fileAttrs.length - 1];
            PutObjectResult putRet = ossClient.putObject(bucketName, objectName, new File(filePath));
            log.info("上传语音文件结果 {}", putRet.getResponse().getStatusCode());
        }

        return false;
    }

}
