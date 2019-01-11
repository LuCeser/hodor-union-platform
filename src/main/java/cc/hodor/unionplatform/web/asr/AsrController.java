package cc.hodor.unionplatform.web.asr;

import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.asr.IAsrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
 *   zhanglu               2019/1/8-17:33
 *
 ****************************************************************************************/
@Controller
@RequestMapping("v1.0/asr")
public class AsrController {

    private IAsrService asrService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity startAsr(@RequestBody AsrDTO asrDTO) {

        ServiceResult ret = asrService.startAsr(asrDTO);
        if (ret.isSuccess()) {
            return new ResponseEntity(HttpStatus.CREATED);
        }

        return null;
    }

    public AsrController(IAsrService asrService) {
        this.asrService = asrService;
    }
}
