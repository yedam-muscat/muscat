package egovframework.com.muscat.board.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.muscat.board.service.BoardMasterService;
import egovframework.com.muscat.board.service.BoardMasterVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardContoller {
    // 서비스 주입 (게시판 마스터 목록 조회용)
    private final BoardMasterService boardmasgersercice;

    // Thymeleaf 화면 호출 (HTML 페이지)
    // 사용자가 /boardMasterList.do 요청 시, board/boardmaster.html 페이지 렌더링
    @GetMapping("/boardMasterList.do")
    public String bmListPage() {
    	// webapp/WEB-INF/views/board/boardmaster.html
        return "board/boardmaster.html";
    }

    // JSON 데이터 제공 (AJAX 호출)
    // /board/list.json 요청 시 게시판 마스터 목록을 JSON으로 반환
    @GetMapping("/board/list")
    @ResponseBody
    public Map<String, Object> bmListAjax() {
        List<BoardMasterVO> list = boardmasgersercice.boardMasterList();

        Map<String, Object> result = new HashMap<>();
        result.put("resultList", list);
        result.put("resultCnt", list.size());
        return result;
    }
}
