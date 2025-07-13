package egovframework.com.muscat.appr.mapper;

import java.util.List;

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
}
