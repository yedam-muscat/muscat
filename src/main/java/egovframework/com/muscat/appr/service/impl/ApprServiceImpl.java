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

	@Override
	public int regDocForm(DocFormVO docForm) {
		
		return 0;
	}

	@Override
	public List<DocFormVO> getDocFormList(DocFormVO docForm) {
		return null;
	}

}
