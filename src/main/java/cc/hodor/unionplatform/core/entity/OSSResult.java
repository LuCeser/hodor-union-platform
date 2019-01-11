package cc.hodor.unionplatform.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

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
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/10-11:20
 *
 ****************************************************************************************/
@Setter
@Getter
public class OSSResult {

    /**
     * 本次查询文件的起点
     */
    private String marker;

    /**
     * 下一次查询文件的起点
     */
    private String nextMarker;

    /**
     * 是否所有结果都已返回
     */
    private boolean truncated;

    /**
     * 对应录音id
     */
    private long fileId;

    /**
     * 限定返回的url
     */
    private Set<Map> fileUrls;
}
