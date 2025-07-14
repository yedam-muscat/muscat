package egovframework.com.muscat.appr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import egovframework.com.muscat.appr.service.ApprDocSearchVO;
import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.ApprHistoryVO;
import egovframework.com.muscat.appr.service.ApprLineDetailVO;
import egovframework.com.muscat.appr.service.DocFormSearchVO;
import egovframework.com.muscat.appr.service.DocFormVO;

@Mapper
public interface ApprMapper {

	int insertApprLine(ApprDocVO apprDoc);

	int insertApprDoc(ApprDocVO apprDoc);

	int insertApprApprovers(ApprDocVO apprDoc);

	int insertApprReferences(ApprDocVO apprDoc);

	// 결재대기
	List<ApprDocVO> selectApprDoc(ApprDocSearchVO apprDocSearchVO);

	// 결재대기
	int selectApprDocTotCnt(ApprDocSearchVO apprDocSearchVO);

	// 결재요청
	List<ApprDocVO> selectReqAppr(ApprDocSearchVO apprDocSearchVO);

	// 결재요청 수
	int selectReqApprTotCnt(ApprDocSearchVO apprDocSearchVO);

	// 결재요청 상세
	ApprDocVO selectReqApprDetail(String adocId);

	// 결재요청 결재정보
	List<ApprLineDetailVO> selectReqApprLineDetail(String alineId);

	// 결재요청 결재정보
	List<ApprHistoryVO> selectReqApprHistory(String adocId);

	List<String> selectApproversByAlineId(@Param("alineId") String alineId);

	List<ApprDocVO> selectApprDocRecent();

	List<ApprDocVO> selectApprDocHistory();

	// 문서양식 등록
	int insertDocForm(DocFormVO docForm);

	// 문서양식 검색
	List<DocFormVO> selectDocFormList(DocFormSearchVO docFormSearch);

	// 문서양식 검색 수
	int selectDocFormListTotCnt(DocFormSearchVO docFormSearch);

	// 결재처리
	int insertApprHistory(ApprHistoryVO apprHistory);

	// 결재순번 확인
	boolean isLastApprover(ApprHistoryVO apprHistory);

	// 문서 상태 갱신
	int updateApprDoc(Map<String, String> param);
	
	// 문서함 3총사
	List<ApprDocVO> selectRegHistory(ApprDocSearchVO apprDocSearchVO);
	List<ApprDocVO> selectReqHistory(ApprDocSearchVO apprDocSearchVO);
	List<ApprDocVO> selectRefHistory(ApprDocSearchVO apprDocSearchVO);
	
	int regHistoryTotCnt(ApprDocSearchVO apprDocSearchVO);
	int reqHistoryTotCnt(ApprDocSearchVO apprDocSearchVO);
	int refHistoryTotCnt(ApprDocSearchVO apprDocSearchVO);
}
