package egovframework.com.muscat.appr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.appr.mapper.ApprMapper;
import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.ApprService;
import egovframework.com.muscat.appr.service.DocFormSearchVO;
import egovframework.com.muscat.appr.service.DocFormVO;
import egovframework.com.muscat.common.ResultVO;

@Service
public class ApprServiceImpl implements ApprService {

	@Autowired
	ApprMapper apprMapper;

	// 트랜잭션 처리 고려할 것, 오류 처리
	@Override
	public ResultVO regApprDoc(ApprDocVO apprDoc) {
		ResultVO result = new ResultVO();
	
		int count = apprMapper.insertApprLine(apprDoc);
	
		count = apprMapper.insertApprDoc(apprDoc);
		
		count = apprMapper.insertApprApprovers(apprDoc);
		
		count = apprMapper.insertApprReferences(apprDoc);
		
		result.setResultCode("");
		result.setResultMsg("");
		result.setResultSuccess(true);
		
		return result;
	}

	@Override
	public List<ApprDocVO> getApprDocRecent() {
		return apprMapper.selectApprDocRecent();
	}

	@Override
	public List<ApprDocVO> getApprDocHistory() {
		return apprMapper.selectApprDocHistory();
	}

	// 문서양식 등록
	@Override
	public int regDocForm(DocFormVO docForm) {
		return apprMapper.insertDocForm(docForm);
	}

	@Override
	public List<DocFormVO> getDocFormList(DocFormSearchVO docFormSearch) {
		return apprMapper.selectDocFormList(docFormSearch);
	}

	@Override
	public int getDocFormListTotCnt(DocFormSearchVO docFormSearch) {
		return apprMapper.selectDocFormListTotCnt(docFormSearch);
	}
}
