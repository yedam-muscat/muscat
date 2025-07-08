package egovframework.com.muscat.appr.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.ApprService;
import egovframework.com.muscat.appr.service.DocFormVO;
import egovframework.com.muscat.common.ResultVO;

@Controller
public class ApprContoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprContoller.class);

	@Autowired
	ApprService apprService;

	// 전자결재 메인 페이지
	@GetMapping("/appr/apprMain.do")
	public String apprMain() {
		return "appr/apprMain.html";
	}

	// 전자결제 메인 - 최근 기안/결재 문서
	@GetMapping("/appr/getApprMainRecent")
	@ResponseBody
	public List<ApprDocVO> getApprMainRecent() {
		return apprService.getApprDocRecent();
	}

	// 전자결제 메인 - 최근 완료 문서
	@GetMapping("/appr/getApprMainHistory")
	@ResponseBody
	public List<ApprDocVO> getApprMainHistory() {
		return apprService.getApprDocHistory();
	}

	// 문서 기안 페이지
	@GetMapping("/appr/apprReg.do")
	public String apprReg() {
		return "appr/apprReg.html";
	}
	
	// 문서 내역 페이지
	@GetMapping("/appr/apprHistory.do")
	public String apprHistory() {
		return "appr/apprHistory.html";
	}

	// 문서 양식 관련
	
	// 문서 양식 관리 페이지
	@GetMapping("/appr/docFormMng.do")
	public String docFormMng() {
		return "appr/docFormMng.html";
	}

	// 문서 양식 조회
	@GetMapping("/appr/getDocForm.do")
	@ResponseBody
	public String getDocForm() {
		return "";
	}

	// 문서 양식 등록 페이지
	@GetMapping("/appr/docFormReg.do")
	public String docFormReg() {
		return "appr/docFormReg.html";
	}

	// 문서 양식 등록
	@PostMapping("/appr/postDocFormReg.do")
	@ResponseBody
	public ResultVO postDocFormReg(@RequestBody DocFormVO docForm) {
		ResultVO result = new ResultVO();
		int serviceResult = apprService.regDocForm(docForm);
		return result;
	}
}
