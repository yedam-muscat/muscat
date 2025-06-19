package egovframework.com.muscat.chat.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.muscat.chat.service.ChattingService;

@Controller
public class ChattingController {
	
	@Autowired ChattingService chattingService;
	
	@RequestMapping("/chat.do")
//	@ResponseBody
	public String chat() {
		return "chat/" + chattingService.chat();
	}

}