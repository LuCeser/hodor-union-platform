package cc.hodor.unionplatform.dao;

import cc.hodor.unionplatform.model.AuthenticationDO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

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
 *  Header Name: WellJoint
 *
 *  Description: 
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/2-12:09
 *
 ****************************************************************************************/
public interface AuthenticationMapper extends Mapper<AuthenticationDO> {

    long insertCollect(List<AuthenticationDO> authenticationDOList);

    int insertAuth(AuthenticationDO authenticationDO);

    int updateParams(@Param("engine") String engine, @Param("appAccessKey") String appAccessKey,
                     @Param("appAccessSecret") String appAccessSecret, @Param("appId") String appId,
                     @Param("extendInfo") Map extendInfo);

    AuthenticationDO findByEngine(@Param("engine") int engine);
}
