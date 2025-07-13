package egovframework.com.muscat.appr.service;

import java.util.List;

import egovframework.com.muscat.common.ResultVO;

public interface ApprService {
	
	ResultVO regApprDoc(ApprDocVO apprDoc);
	
	List<ApprDocVO> getApprDocRecent();
	
	List<ApprDocVO> getApprDocHistory();
	
	// 문서양식 등록
	int regDocForm(DocFormVO docForm);
	
	// 문서양식 검색
	List<DocFormVO> getDocFormList(DocFormSearchVO docForm);
	
	// 문서양식 검색 수
	int getDocFormListTotCnt(DocFormSearchVO docForm);
	
}
