package cc.hodor.unionplatform.service.authentication;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.dao.AuthenticationMapper;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.web.authentication.AuthenticationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/2-14:31
 *
 ****************************************************************************************/
@Slf4j
@Service("authenticationService")
public class AuthenticationServiceServiceImpl implements IAuthenticationService {

    private AuthenticationMapper authenticationMapper;

    @Override
    public ServiceResult createAuthentication(AuthenticationDTO authenticationDTO) {
        AuthenticationDO authenticationDO = new AuthenticationDO();
        authenticationDO.setId(UUID.randomUUID().toString());
        try {
            BeanUtils.copyProperties(authenticationDO, authenticationDTO);
        } catch (IllegalAccessException e) {
            log.error("无法访问bean属性", e);
        } catch (InvocationTargetException e) {
            log.error("设置属性失败", e);
        }

        ServiceResult<Integer> ret = new ServiceResult<>();
        int updates = authenticationMapper.insertAuth(authenticationDO);
        if (updates > 0) {
            log.info("插入数据权信息");
            ret.setSuccess(true);
            ret.setData(updates);
        } else {
            ret.setSuccess(false);
        }
        return ret;
    }

    @Override
    public ServiceResult createAuthentication(List<AuthenticationDTO> authenticationDTOList) {
        ServiceResult ret = new ServiceResult();
        for (AuthenticationDTO authenticationDTO:authenticationDTOList) {
            ret = createAuthentication(authenticationDTO);
            if (!ret.isSuccess()) {
                log.warn("插入失败 {}, {}", authenticationDTO.getEngine(), authenticationDTO.getAppId());
                break;
            }
        }
        return ret;
    }

    @Override
    public ServiceResult updateAuthentication(String engine, Map param) {

        return null;
    }

    @Override
    public ServiceResult getAllAuthentication() {
        return null;
    }

    @Override
    public ServiceResult<AuthenticationDO> getAuthentication(VendorEnum engine) {
        ServiceResult ret = new ServiceResult();
        AuthenticationDO authenticationDO = authenticationMapper.findByEngine(engine.getCode());
        if (authenticationDO != null) {
            log.info("找到记录 {}", engine);
            ret.setSuccess(true);
            ret.setData(authenticationDO);
        } else {
            log.info("未找到记录 {}", engine);
            ret.setSuccess(false);
        }
        return ret;
    }

    @Override
    public ServiceResult deleteAuthentication(String engine) {
        return null;
    }

    @Autowired
    public AuthenticationServiceServiceImpl(AuthenticationMapper authenticationMapper) {
        this.authenticationMapper = authenticationMapper;
    }
}
