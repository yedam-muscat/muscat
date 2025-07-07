package egovframework.com.muscat.appr.service;

import java.util.List;

public interface ApprService {

	String test();
	
	List<ApprDocVO> getApprDocRecent();
	
	List<ApprDocVO> getApprDocHistory();
	

	int regDocForm(DocFormVO docForm);
	
	List<DocFormVO> getDocFormList(DocFormVO docForm);
	
	
}
