package egovframework.com.muscat.appr.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.appr.mapper.ApprMapper;
import egovframework.com.muscat.appr.service.ApprDocSearchVO;
import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.ApprHistoryVO;
import egovframework.com.muscat.appr.service.ApprLineDetailVO;
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

//		count = apprMapper.insertApprReferences(apprDoc);

		result.setResultCode("");
		result.setResultMsg("");
		result.setResultSuccess(true);

		return result;
	}

	@Override
	public List<ApprDocVO> readyAppr(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectApprDoc(apprDocSearchVO);
	}

	@Override
	public int readyApprTotCnt(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectApprDocTotCnt(apprDocSearchVO);
	}

	@Override
	public List<ApprDocVO> reqAppr(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectReqAppr(apprDocSearchVO);
	}

	@Override
	public int reqApprTotCnt(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectReqApprTotCnt(apprDocSearchVO);
	}

	@Override
	public ApprDocVO reqApprDetail(String adocId) {
		return apprMapper.selectReqApprDetail(adocId);
	}

	@Override
	public List<ApprLineDetailVO> reqApprLineDetail(String alineId) {

		return apprMapper.selectReqApprLineDetail(alineId);
	}

	@Override
	public List<ApprHistoryVO> reqApprHistory(String adocId) {
		return apprMapper.selectReqApprHistory(adocId);
	}

	// 메인화면 및 문서함

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

	// 결재처리
	@Override
	public int handleAppr(ApprHistoryVO apprHistory) {
		return apprMapper.insertApprHistory(apprHistory);
	}

	// 결재순번 확인
	@Override
	public boolean isLastAppr(ApprHistoryVO apprHistory) {
		return apprMapper.isLastApprover(apprHistory);
	}

	// 문서 상태 갱신
	@Override
	public int modifyApprDoc(Map<String, String> param) {
		return apprMapper.updateApprDoc(param);
	}

	// 문서함 3총사
	@Override
	public List<ApprDocVO> regHistory(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectRegHistory(apprDocSearchVO);
	}

	@Override
	public List<ApprDocVO> reqHistory(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectReqHistory(apprDocSearchVO);
	}

	@Override
	public List<ApprDocVO> refHistory(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.selectRefHistory(apprDocSearchVO);
	}

	@Override
	public int regHistoryTotCnt(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.regHistoryTotCnt(apprDocSearchVO);
	}

	@Override
	public int reqHistoryTotCnt(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.reqHistoryTotCnt(apprDocSearchVO);
	}

	@Override
	public int refHistoryTotCnt(ApprDocSearchVO apprDocSearchVO) {
		return apprMapper.refHistoryTotCnt(apprDocSearchVO);
	}

}
