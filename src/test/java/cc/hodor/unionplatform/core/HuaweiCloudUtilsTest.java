package cc.hodor.unionplatform.core;

import cc.hodor.unionplatform.AppTests;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.authentication.IAuthenticationService;
import cc.hodor.unionplatform.util.FileUtils;
import com.huawei.ais.sdk.AisAccess;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class HuaweiCloudUtilsTest extends AppTests {

    @Autowired
    private IAuthenticationService authenticationService;

    @Test
    public void getAccessService() {
    }

    @Test
    public void asr() {

        ServiceResult result = authenticationService.getAuthentication(VendorEnum.HUAWEI);
        if (result.isSuccess()) {
            AuthenticationDO authenticationDO = (AuthenticationDO) result.getData();
            AisAccess aisAccess = HuaweiCloudUtils.getAccessService(authenticationDO.getAppAccessKey(),
                    authenticationDO.getAppAccessSecret());

            List<String> files = FileUtils.getListFiles("E:\\voice", "", false);

            List<String> jobIds = HuaweiCloudUtils.asr(aisAccess, files);
            if (jobIds.size() > 0) {
                for (String jobId : jobIds) {
                    HuaweiCloudUtils.getAsrResult(aisAccess, jobId);
                }
            }
        }


    }

    @Test
    public void getAsrResult() {
    }
}