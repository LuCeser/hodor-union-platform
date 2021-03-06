package cc.hodor.unionplatform.service.authentication;

import cc.hodor.unionplatform.AppTests;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.web.authentication.AuthenticationDTO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationServiceServiceImplTest extends AppTests {

    @Autowired
    private IAuthenticationService authenticationService;

    @Test
    public void createAuthentication() {

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEngine(VendorEnum.ALI);
        authenticationDTO.setAppId("123");
        authenticationDTO.setAppAccessKey("a123");
        authenticationDTO.setAppAccessSecret("ABC");

        ServiceResult ret = authenticationService.createAuthentication(authenticationDTO);
        Assert.assertTrue(ret.isSuccess());

    }
}