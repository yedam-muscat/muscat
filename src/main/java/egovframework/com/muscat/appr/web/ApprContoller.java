package egovframework.com.muscat.appr.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.appr.service.ApprService;
import egovframework.com.muscat.appr.service.DocFormVO;
import egovframework.com.muscat.common.ResultVO;

@Controller
public class ApprContoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprContoller.class);

	@Autowired
	ApprService apprService;

	// 문서기안
	@GetMapping("/appr/regAppr.do")
	public String regAppr() {
		return "appr/regAppr.html";
	}

	// 임시저장
	@GetMapping("/appr/tempAppr.do")
	public String tempAppr() {
		return "appr/tempAppr.html";
	}

	// 결재대기
	@GetMapping("/appr/readyAppr.do")
	public String readyAppr() {
		return "appr/readyAppr.html";
	}
	
	// 결재요청
	@GetMapping("/appr/reqAppr.do")
	public String reqAppr() {
		return "appr/reqAppr.html";
	}
	
	// 문서함
	// 기안문서함
	@GetMapping("/appr/regHistory.do")
	public String regHistory() {
		return "appr/regHistory.html";
	}
	
	// 결재문서함
	@GetMapping("/appr/reqHistory.do")
	public String reqHistory() {
		return "appr/reqHistory.html";
	}
	
	// 참조문서함
	@GetMapping("/appr/refHistory.do")
	public String refHistory() {
		return "appr/refHistory.html";
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
	@PostMapping("/appr/docFormReg.do")
	@ResponseBody
	public ResultVO postDocFormReg(@RequestBody DocFormVO docForm) {
		ResultVO result = new ResultVO();
		int count = apprService.regDocForm(docForm);
		
		if (count > 0) {
			result.setResultCode("");
			result.setResultMsg("문서양식이 등록되었습니다");
			result.setResultSuccess(true);
		} else {
			result.setResultCode("");
			result.setResultMsg("문서양식 등록 중 오류가 발생했습니다");
			result.setResultSuccess(false);
		}
		
		return result;
	}
}
