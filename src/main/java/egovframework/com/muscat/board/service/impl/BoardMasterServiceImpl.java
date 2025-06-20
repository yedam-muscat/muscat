package egovframework.com.muscat.board.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.board.service.BoardMaster;
import egovframework.com.muscat.board.service.BoardMasterService;
import egovframework.com.muscat.board.service.BoardMasterVO;

@Service
public class BoardMasterServiceImpl extends EgovAbstractServiceImpl implements BoardMasterService {

	@Autowired
	private BoardMasterDAO boardMasterDao;
	
	@Override
	public String board() {
		return "board";
	}

	@Override
	public Map<String, Object> selectNotUsedBdMstrList(BoardMasterVO boardMasterVO) {
		return null;
	}

	@Override
	public void deleteBBSMasterInf(BoardMaster boardMaster) {
		
	}

	@Override
	public void updateBBSMasterInf(BoardMaster boardMaster) throws Exception {

	}

	@Override
	public BoardMasterVO selectBBSMasterInf(BoardMasterVO boardMasterVO) throws Exception {
		return null;
	}

	@Override
	public Map<String, Object> selectBBSMasterInfs(BoardMasterVO boardMasterVO) {
		List<BoardMasterVO> resultList = boardMasterDao.selectBBSMasterInfs(boardMasterVO);
		int cnt = boardMasterDao.selectBBSMasterInfsCnt(boardMasterVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("resultList", resultList );
		resultMap.put("resultCnt", cnt);
		return resultMap;
	}

	@Override
	public void insertBBSMasterInf(BoardMaster boardMaster) throws Exception {

	}

}
