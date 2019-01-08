package cc.hodor.unionplatform.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
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
        try {
            for (String filePath:filePathList) {
                String[] fileAttrs = filePath.split("/");
                String objectName = fileAttrs[fileAttrs.length - 1];
                ossClient.putObject(bucketName, objectName, new File(filePath));
            }
        } catch (OSSException e) {
            log.error("上传文件至OSS失败");
            return false;
        } catch (ClientException e) {
            log.error("上传文件至OSS失败");
            return false;
        } finally {
            ossClient.shutdown();
        }

        log.info("上传文件值ALI OSS成功");
        return true;
    }

}
