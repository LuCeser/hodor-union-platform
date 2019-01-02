package cc.hodor.unionplatform.service.authentication;

import cc.hodor.unionplatform.AppTests;
import cc.hodor.unionplatform.base.VendorEnum;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.web.authentication.AuthenticationDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

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

        authenticationService.createAuthentication(authenticationDTO);

    }
}