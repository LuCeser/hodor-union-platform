package cc.hodor.unionplatform.util;

import cc.hodor.unionplatform.core.entity.OSSResult;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
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

    /**
     * 上传文件至阿里云OSS服务
     *
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @param bucketName
     * @param prefix
     * @param filePathList
     * @return
     */
    public static boolean putFiles(String endpoint, String accessKeyId, String accessKeySecret,
                                   String bucketName, String prefix, List<String> filePathList) {

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        long current = System.currentTimeMillis();
        log.info("开始上传文件");
        try {
            for (String filePath : filePathList) {
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

        log.info("上传文件至ALI OSS成功, 耗时: {}", System.currentTimeMillis() - current);
        return true;
    }

    public static OSSResult generatePresignedUrls(final String endpoint, final String accessKeyId, final String accessKeySecret,
                                                  final String bucketName, final int maxFiles, final Date expiration) {

        OSSResult ossResult = new OSSResult();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {

            Set<String> presignedUrls = new HashSet<>(maxFiles);

            ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withMaxKeys(maxFiles));
            String marker = objectListing.getMarker();
            String nextMarker = objectListing.getNextMarker();
            boolean isTruncated = objectListing.isTruncated();

            List<OSSObjectSummary> summaries = objectListing.getObjectSummaries();
            for (OSSObjectSummary summary : summaries) {
                URL url = ossClient.generatePresignedUrl(bucketName, summary.getKey(), expiration);
                log.debug("文件名: {}, 生成临时url: {}", summary.getKey(), url.toString());
                presignedUrls.add(url.toString());
            }

            ossResult.setMarker(marker);
            ossResult.setNextMarker(nextMarker);
            ossResult.setPresignedUrls(presignedUrls);
            ossResult.setTruncated(isTruncated);
        } catch (OSSException e) {
            log.error("生成签名URL失败");
        } catch (ClientException e) {
            log.error("上传文件至OSS失败");
        } finally {
            ossClient.shutdown();
        }

        return ossResult;
    }

}
