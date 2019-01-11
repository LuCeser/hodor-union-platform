package cc.hodor.unionplatform.dao;

import cc.hodor.unionplatform.model.RecordDO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

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
 *   zhanglu               2019/1/11-15:14
 *
 ****************************************************************************************/
public interface RecordMapper extends Mapper<RecordDO> {

    RecordDO findByUid(@Param("uniqueId") String uid);

}
