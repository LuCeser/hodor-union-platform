package cc.hodor.unionplatform.core;

import cc.hodor.unionplatform.service.asr.IAsrService;
import cc.hodor.unionplatform.web.asr.AsrDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
 *   zhanglu               2019/1/18-11:59
 *
 ****************************************************************************************/
@Slf4j
@RequiredArgsConstructor
public class TaskProducer implements Runnable {

    @NonNull
    private AsrDTO asrDTO;

    @NonNull
    private IAsrService asrService;


    @Override
    public void run() {
        asrService.longSentenceRecognition(asrDTO);
    }

}
