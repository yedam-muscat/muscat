package egovframework.com.muscat.appr.service;

import java.util.List;

import egovframework.com.muscat.common.ResultVO;

public interface ApprService {

	// 문서기안
	ResultVO regApprDoc(ApprDocVO apprDoc);

	// 결재대기
	List<ApprDocVO> readyAppr(ApprDocSearchVO apprDocSearchVO);

	// 결재대기 수
	int readyApprTotCnt(ApprDocSearchVO apprDocSearchVO);

	// 결재요청
	List<ApprDocVO> reqAppr(ApprDocSearchVO apprDocSearchVO);

	// 결재요청 수
	int reqApprTotCnt(ApprDocSearchVO apprDocSearchVO);

	ApprDocVO reqApprDetail(String adocId);

	List<ApprLineDetailVO> reqApprLineDetail(String alineId);

	List<ApprHistoryVO> reqApprHistory(String adocId);

	List<ApprDocVO> getApprDocRecent();

	List<ApprDocVO> getApprDocHistory();

	// 문서양식 등록
	int regDocForm(DocFormVO docForm);

	// 문서양식 검색
	List<DocFormVO> getDocFormList(DocFormSearchVO docForm);

	// 문서양식 검색 수
	int getDocFormListTotCnt(DocFormSearchVO docForm);

}
