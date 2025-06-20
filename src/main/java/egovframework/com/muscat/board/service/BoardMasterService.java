package egovframework.com.muscat.board.service;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;

import egovframework.com.cmm.LoginVO;

/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일			수정자		수정내용
 *  -------			--------	---------------------------
 *   2024.10.29		inganyoyo	Controller는 Transaction 처리를 하지 않아 Controller에서 오류 발생 시 데이터 정합성 오류 문제 발생
 * </pre>
 */

public interface BoardMasterService {

	String board();

	Map<String, Object> selectNotUsedBdMstrList(BoardMasterVO boardMasterVO);

	void deleteBBSMasterInf(BoardMaster boardMaster);

	void updateBBSMasterInf(BoardMaster boardMaster) throws Exception;

	BoardMasterVO selectBBSMasterInf(BoardMasterVO boardMasterVO) throws Exception;

	Map<String, Object> selectBBSMasterInfs(BoardMasterVO boardMasterVO);

	void insertBBSMasterInf(BoardMaster boardMaster) throws Exception;
}
