package egovframework.com.muscat.board.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    @Resource(name = "EgovBBSMasterService")
    private EgovBBSMasterService bbsService;

    /** 모든 화면에서 사용할 게시판 목록 (USE_AT = 'Y') */
    @ModelAttribute("boardList")
    public List<BoardMasterVO> boardList() throws Exception {
        BoardMasterVO vo = new BoardMasterVO();
        vo.setUseAt("Y");
        return bbsService.selectBBSListPortlet(vo);
    }

    /** 현재 로그인 사용자의 ESNTL_ID(= uniqId) ― 없으면 공백 문자열 반환 */
    @ModelAttribute("loginUserId")
    public String loginUserId() {
        if (EgovUserDetailsHelper.isAuthenticated()) {
            LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
            return user != null ? user.getUniqId() : "";
        }
        return "";
    }
}
