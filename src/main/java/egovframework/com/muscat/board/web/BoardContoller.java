package egovframework.com.muscat.board.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.muscat.board.service.BoardMasterService;
import egovframework.com.muscat.board.service.BoardMasterVO;

@Controller
public class BoardContoller {
	
	@Autowired
	private BoardMasterService boardService;
	
	@Autowired
    private DefaultBeanValidator beanValidator;
	
	@RequestMapping(value="/board.do")
	public String board() {
		return "board/" + boardService.board();
	}
	
	@RequestMapping(value="/api/boardList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> boardList(BoardMasterVO vo) throws Exception {
		return null;	
	}
	
	

}
