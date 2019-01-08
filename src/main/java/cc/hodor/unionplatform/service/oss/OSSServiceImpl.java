package cc.hodor.unionplatform.service.oss;

import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.authentication.IAuthenticationService;
import cc.hodor.unionplatform.util.FileUtils;
import cc.hodor.unionplatform.util.OSSUtils;
import cc.hodor.unionplatform.web.oss.OSSDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
 *   zhanglu               2019/1/7-16:21
 *
 ****************************************************************************************/
@Slf4j
@Service("ossService")
public class OSSServiceImpl implements IOSSService {

    private IAuthenticationService authenticationService;

    @Override
    public ServiceResult uploadFile(OSSDTO ossDTO) {

        ServiceResult ret = new ServiceResult(false);
        ServiceResult authResult = authenticationService.getAuthentication(ossDTO.getEngine());
        if (authResult.isSuccess()) {
            List<String> filePathList = FileUtils.getListFiles(ossDTO.getFilePath(), "", false);
            log.info("file : {}", filePathList.size());
            AuthenticationDO authenticationDO = (AuthenticationDO) authResult.getData();
            Map extendInfo = authenticationDO.getExtendInfo();
            String endpoint = (String) extendInfo.get("endpoint");
            String bucketName = (String) extendInfo.get("bucket");
            boolean success = OSSUtils.putFiles(endpoint, authenticationDO.getAppAccessKey(), authenticationDO.getAppAccessSecret(),
                    bucketName, "", filePathList);

            if (success) {
                log.info("传输文件成功");
                ret.setSuccess(true);
            }
        }
        return ret;
    }

    public OSSServiceImpl(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
