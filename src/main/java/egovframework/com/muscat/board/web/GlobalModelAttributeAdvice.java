package egovframework.com.muscat.board.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    @Resource(name = "EgovBBSMasterService")
    private EgovBBSMasterService bbsService;

    /**
     * 모든 화면에서 사용할 게시판 목록 (USE_AT='Y' 만)
     */
    @ModelAttribute("boardList")
    public List<BoardMasterVO> boardList() throws Exception {
        BoardMasterVO vo = new BoardMasterVO();
        vo.setUseAt("Y");
        return bbsService.selectBBSListPortlet(vo);   // 이미 존재하는 DAO/SQL 재사용
    }
}
