package egovframework.com.muscat.board.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.board.mapper.BoardMasterMapper;
import egovframework.com.muscat.board.service.BoardMasterService;
import egovframework.com.muscat.board.service.BoardMasterVO;

@Service
//@AllArgsConstructor
public class BoardMasterServiceImpl implements BoardMasterService {
	
	@Autowired
	private BoardMasterMapper boardmastermapper;

	@Override
	public List<BoardMasterVO> boardMasterList() {
		return boardmastermapper.boardMasterList();
	}

   
}
