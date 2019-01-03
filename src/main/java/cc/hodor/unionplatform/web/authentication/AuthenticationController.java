package cc.hodor.unionplatform.web.authentication;

import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.authentication.IAuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
 *   zhanglu               2019/1/2-11:57
 *
 ****************************************************************************************/
@Controller
@RequestMapping("v1.0/authentication")
public class AuthenticationController {

    private IAuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity createAuthentication(@RequestBody List<AuthenticationDTO> authenticationDTOS) {
        ServiceResult ret = authenticationService.createAuthentication(authenticationDTOS);
        if (ret.isSuccess()) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(ret.getData(), HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateAuthentication(@RequestParam("engine") String engine,
                                               @RequestParam("field") String fields) {

        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAuthentication(@RequestParam(value = "engine", required = false) String engine) {
        return  null;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAuthentication(@RequestParam(value = "engine", required = false) String engine) {
        return null;
    }

    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
