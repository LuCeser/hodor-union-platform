package cc.hodor.unionplatform.core.entity;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.base.entity.Sentence;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
 *   zhanglu               2019/1/11-9:09
 *
 ****************************************************************************************/
@Setter
@Getter
public class RecognitionResult {

    private VendorEnum engine;

    private AsrStatusEnum status;

    private long duration;

    private long fileId;

    private List<Sentence> sentences;

    private long recognitionDuration;

}
