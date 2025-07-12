package egovframework.com.muscat.appr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import egovframework.com.muscat.appr.service.ApprDocVO;
import egovframework.com.muscat.appr.service.DocFormSearchVO;
import egovframework.com.muscat.appr.service.DocFormVO;

@Mapper
public interface ApprMapper {
	
	String selectTime();
	
	List<ApprDocVO> selectApprDocRecent();
	
	List<ApprDocVO> selectApprDocHistory();
	
	
	
	// 문서양식 등록
	int insertDocForm(DocFormVO docForm);
	
	// 문서양식 검색
	List<DocFormVO> selectDocFormList(DocFormSearchVO docFormSearch);
	
	// 문서양식 검색 수
	int selectDocFormListTotCnt(DocFormSearchVO docFormSearch);
}
