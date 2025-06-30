package egovframework.com.muscat.common.web;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovEnvCryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.web.EgovFileMngController;

@Controller
public class FileContoller {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovFileMngController.class);
	
	/** 암호화서비스 */
	private static EgovEnvCryptoService cryptoService;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;

	@Resource(name = "egovEnvCryptoService")
	public void setEgovEnvCryptoService(EgovEnvCryptoService cryptoService) {
		FileContoller.cryptoService = cryptoService;
	}

	@RequestMapping("/file/selectFileInfs.do")
	@ResponseBody
	public Map<String, Object> selectFileInfs(@ModelAttribute("searchVO") FileVO fileVO,
			HttpServletRequest request,
			@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String param_atchFileId = (String) commandMap.get("param_atchFileId");
		String decodedAtchFileId = "";
		
		if (param_atchFileId != null && !"".equals(param_atchFileId) ) {
			decodedAtchFileId = cryptoService.decrypt(param_atchFileId);
		}
		
		fileVO.setAtchFileId(decodedAtchFileId);
		List<FileVO> result = fileService.selectFileInfs(fileVO);

		// FileId를 유추하지 못하도록 세션ID와 함께 암호화하여 표시한다. (2022.12.06 추가) - 파일아이디가 유추 불가능하도록 조치
		for (FileVO file : result) {
			String sessionId = request.getSession().getId();
			String toEncrypt = sessionId + "|" + file.atchFileId;
			file.setAtchFileId(Base64.getEncoder().encodeToString(cryptoService.encrypt(toEncrypt).getBytes()));
		}
		
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("fileList", result);
		resultMap.put("updateFlag", "N");
		resultMap.put("fileListCnt", result.size());
		resultMap.put("atchFileId", param_atchFileId);

		return resultMap;
	}

}
