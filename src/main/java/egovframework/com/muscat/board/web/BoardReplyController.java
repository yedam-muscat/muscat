package egovframework.com.muscat.board.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.cmt.service.Comment;
import egovframework.com.cop.cmt.service.CommentVO;
import egovframework.com.cop.cmt.service.EgovArticleCommentService;

@Controller
public class BoardReplyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoardReplyController.class);

	@Resource(name = "EgovArticleCommentService")
	private EgovArticleCommentService commentService;

	@Resource(name = "propertiesService")
	private EgovPropertyService propertyService;

	 /** 📝 댓글 목록 요청 화면 */
    @GetMapping("/board/replyList.do")
    public String replyListPage(@RequestParam("bbsId") String bbsId, @RequestParam("nttId") String nttId, Model model) {
        model.addAttribute("bbsId", bbsId);
        model.addAttribute("nttId", nttId);
        return "board/replyList.html";
    }

    /** ✅ 댓글 목록 조회 (AJAX) */
    @GetMapping("/board/replyList")
    @ResponseBody
    public Map<String, Object> commentList(
            @RequestParam String bbsId,
            @RequestParam String nttId,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "5") int pageSize
    ) throws Exception {
        Map<String, Object> result = new HashMap<>();

        CommentVO searchVO = new CommentVO();
        searchVO.setBbsId(bbsId);
        searchVO.setNttId(Long.parseLong(nttId));
        searchVO.setSubPageIndex(pageIndex);               // ✅ 현재 페이지
        searchVO.setSubPageUnit(pageSize);                 // ✅ 한 페이지당 댓글 수
        searchVO.setSubPageSize(1);                        // 페이지 블록 수

        // 페이징 계산
        int firstIndex = (pageIndex - 1) * pageSize;
        searchVO.setSubFirstIndex(firstIndex);
        searchVO.setSubLastIndex(firstIndex + pageSize);
        searchVO.setSubRecordCountPerPage(pageSize);

        Map<String, Object> map = commentService.selectArticleCommentList(searchVO);
        int totalCount = Integer.parseInt((String) map.get("resultCnt"));

        result.put("status", "success");
        result.put("comments", map.get("resultList"));
        result.put("totalCount", totalCount);       // ✅ 총 댓글 수
        result.put("pageIndex", pageIndex);
        result.put("pageSize", pageSize);

        return result;
    }

    /** ✅ 댓글 등록 처리 (AJAX) */
    @PostMapping("/board/replyInsert")
    @ResponseBody
    public Map<String, Object> insertComment(@RequestBody Comment comment) throws Exception {
        Map<String, Object> result = new HashMap<>();

        LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

        if (!isAuthenticated) {
            result.put("status", "fail");
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        comment.setFrstRegisterId(user.getUniqId());
        comment.setWrterId(user.getUniqId());
        comment.setWrterNm(user.getName());

        commentService.insertArticleComment(comment);

        result.put("status", "success");
        result.put("message", "댓글이 등록되었습니다.");
        return result;
    }

    /** ✏ 댓글 수정 처리 (AJAX) */
    @PostMapping("/board/replyUpdate")
    @ResponseBody
    public Map<String, Object> updateComment(@RequestBody Comment comment) throws Exception {
        Map<String, Object> result = new HashMap<>();

        LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

        if (!isAuthenticated) {
            result.put("status", "fail");
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        comment.setLastUpdusrId(user.getUniqId());
        commentService.updateArticleComment(comment);

        result.put("status", "success");
        result.put("message", "댓글이 수정되었습니다.");
        return result;
    }

    /** ❌ 댓글 삭제 처리 (AJAX) */
    @PostMapping("/board/replyDelete")
    @ResponseBody
    public Map<String, Object> deleteComment(@RequestBody CommentVO commentVO) throws Exception {
        Map<String, Object> result = new HashMap<>();

        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
        if (!isAuthenticated) {
            result.put("status", "fail");
            result.put("message", "로그인이 필요합니다.");
            return result;
        }

        commentService.deleteArticleComment(commentVO);

        result.put("status", "success");
        result.put("message", "댓글이 삭제되었습니다.");
        return result;
    }
}
