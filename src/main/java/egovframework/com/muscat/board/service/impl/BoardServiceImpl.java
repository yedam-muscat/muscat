package egovframework.com.muscat.board.service.impl;

import org.springframework.stereotype.Service;

import egovframework.com.muscat.board.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {

	@Override
	public String board() {
		return "board";
	}

}
