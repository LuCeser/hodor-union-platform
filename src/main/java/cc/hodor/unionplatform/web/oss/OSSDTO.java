package cc.hodor.unionplatform.web.oss;

import cc.hodor.unionplatform.base.VendorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
 *   zhanglu               2019/1/7-16:02
 *
 ****************************************************************************************/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OSSDTO {

    /**
     * 需要上传至oss的文件目录
     */
    private String filePath;

    /**
     * 需要上传的文件服务
     */
    private VendorEnum engine;

}
