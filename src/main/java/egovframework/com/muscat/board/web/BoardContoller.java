package egovframework.com.muscat.board.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.muscat.board.service.BoardService;

@Controller
public class BoardContoller {
	
	@Autowired BoardService boardService;
	
	@RequestMapping(value="/board.do")
//	@ResponseBody
	public String board() {
		return "egovframework/muscat/board/" + boardService.board();
	}

}
