package egovframework.com.muscat.board.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.muscat.board.service.BoardMasterService;

@Controller
public class BoardContoller {
	
	@Resource(name = "BoardMasterService")
	private BoardMasterService boardService;
	
	@Autowired
    private DefaultBeanValidator beanValidator;
	
	@RequestMapping(value="/board.do")
	public String board() {
		return "board/" + boardService.board();
	}
	
	@RequestMapping(value="/api/boardList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Obuect> boardList(BoardMasterVO vo) throws Exception {
		return boardService.select
	}
	
	

}
