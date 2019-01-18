package cc.hodor.unionplatform.service.asr;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.model.RecordDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.web.asr.AsrDTO;

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
 *   zhanglu               2019/1/10-9:41
 *
 ****************************************************************************************/
public interface IAsrService {
    ServiceResult startAsr(AsrDTO asrDTO);

    ServiceResult stopAsr(VendorEnum vendorEnum);

    void saveRecognitionResult(RecognitionResult recognitionResult);

    void longSentenceRecognition(AsrDTO asrDTO);

    RecordDO findFileId(String uid);
}
