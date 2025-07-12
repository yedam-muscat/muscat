package egovframework.com.muscat.appr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.appr.mapper.ApprMapper;
import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.ApprService;
import egovframework.com.muscat.appr.service.DocFormVO;

@Service
public class ApprServiceImpl implements ApprService {

	@Autowired
	ApprMapper apprMapper;

	@Override
	public String test() {
		return apprMapper.selectTime();
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
	public List<DocFormVO> getDocFormList(DocFormVO docForm) {
		return null;
	}

	@Override
	public int getDocFormListTotCnt(DocFormVO docForm) {
		return 0;
	}

}
