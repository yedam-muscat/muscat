package egovframework.com.muscat.appr.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.ApprService;

@Controller
public class ApprContoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprContoller.class);

	@Autowired
	ApprService apprService;

	@RequestMapping("/appr/apprMain.do")
	public String apprMain() {
		return "appr/apprMain.html";
	}

	@RequestMapping("/appr/apprMainRecent")
	@ResponseBody
	public List<ApprDocVO> apprMainRecent() {
		return apprService.getApprDocRecent();
	}

	@RequestMapping("/appr/apprMainHistory")
	@ResponseBody
	public List<ApprDocVO> apprMainHistory() {
		return apprService.getApprDocHistory();
	}

	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		return apprService.test();
	}
}
