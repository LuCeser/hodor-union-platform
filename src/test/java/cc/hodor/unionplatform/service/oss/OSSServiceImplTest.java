package cc.hodor.unionplatform.service.oss;

import cc.hodor.unionplatform.AppTests;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.web.oss.OSSDTO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OSSServiceImplTest extends AppTests {

    @Autowired
    private IOSSService ossService;

    @Test
    public void uploadFile() {

        OSSDTO ossdto =  new OSSDTO();
        ossdto.setEngine(VendorEnum.ALI);
        ossdto.setFilePath("E:\\voice");

        ServiceResult ret = ossService.uploadFile(ossdto);
        Assert.assertTrue(ret.isSuccess());
    }
}