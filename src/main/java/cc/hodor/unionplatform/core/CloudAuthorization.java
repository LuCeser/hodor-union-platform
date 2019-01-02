package cc.hodor.unionplatform.core;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

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
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2018/12/31-17:07
 *
 ****************************************************************************************/
@Slf4j
public class CloudAuthorization {


    /**
     * 阿里云权限认证, 调用阿里云SDK
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param regionId
     * @param endpointName
     * @param product
     * @param domain
     * @return
     */
    public IAcsClient getAliClient(String accessKeyId, String accessKeySecret,
                          String regionId, String endpointName, String product, String domain) {

        IAcsClient aliClient = null;

        try {
            // 设置endpoint
            DefaultProfile.addEndpoint(endpointName, regionId, product, domain);
            DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            aliClient = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error("设置阿里云EndPoint信息失败");
        }

        return aliClient;
    }
}
