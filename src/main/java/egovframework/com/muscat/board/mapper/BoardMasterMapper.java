package egovframework.com.muscat.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import egovframework.com.muscat.board.service.BoardMasterVO;

@Mapper
public interface BoardMasterMapper {
	
	public List<BoardMasterVO> boardMasterList();
}
