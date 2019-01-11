package cc.hodor.unionplatform.dao;

import cc.hodor.unionplatform.model.RecognitionResultDO;
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
 *   zhanglu               2019/1/10-15:39
 *
 ****************************************************************************************/
public interface RecognitionResultMapper extends Mapper<RecognitionResultDO> {

    int insertResult(RecognitionResultDO recognitionResultDO);

}
