package egovframework.com.muscat.appr.service;

import java.util.List;

public interface ApprService {

	String test();
	
	List<ApprDocVO> getApprDocRecent();
	
	List<ApprDocVO> getApprDocHistory();
	
	// 문서양식 등록
	int regDocForm(DocFormVO docForm);
	
	List<DocFormVO> getDocFormList(DocFormVO docForm);
	
}
