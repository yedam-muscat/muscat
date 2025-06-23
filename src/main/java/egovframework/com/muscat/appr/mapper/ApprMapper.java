package egovframework.com.muscat.appr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import egovframework.com.muscat.appr.service.ApprDocVO;

@Mapper
public interface ApprMapper {
	
	String selectTime();
	
	List<ApprDocVO> selectApprDocRecent();
	
	List<ApprDocVO> selectApprDocHistory();
	
}
