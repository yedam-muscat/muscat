package egovframework.com.muscat.board.web;

import java.util.*;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.cmt.service.Comment;
import egovframework.com.cop.cmt.service.CommentVO;
import egovframework.com.cop.cmt.service.EgovArticleCommentService;

@Controller
public class BoardReplyController {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(BoardReplyController.class);

    @Resource(name = "EgovArticleCommentService")
    private EgovArticleCommentService commentService;

    @Resource(name = "propertiesService")
    private EgovPropertyService propertyService;

    /* ───────────────────────────────────────────────
       1. 댓글 목록 JSON  – currentUserId 필드 포함
    ─────────────────────────────────────────────── */
    @GetMapping("/board/replyList")
    @ResponseBody
    public Map<String,Object> commentList(@RequestParam String bbsId,
                                          @RequestParam String nttId,
                                          @RequestParam(defaultValue="1") int pageIndex,
                                          @RequestParam(defaultValue="5") int pageSize) throws Exception {

        CommentVO search = new CommentVO();
        search.setBbsId(bbsId);
        search.setNttId(Long.parseLong(nttId));
        search.setSubFirstIndex((pageIndex-1)*pageSize);
        search.setSubRecordCountPerPage(pageSize);

        @SuppressWarnings("unchecked")
        Map<String,Object> map = commentService.selectArticleCommentList(search);

        int total = 0;
        try { total = Integer.parseInt((String)map.get("resultCnt")); } catch(Exception ignore){}

        /* 로그인 사용자 ID 확보 → 없으면 빈 문자열 */
        String currentId = "";
        if (EgovUserDetailsHelper.isAuthenticated()) {
            LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
            currentId = user.getUniqId();
        }

        Map<String,Object> res = new HashMap<>();
        res.put("status",      "success");
        res.put("comments",    map.getOrDefault("resultList", List.of()));
        res.put("totalCount",  total);
        res.put("pageIndex",   pageIndex);
        res.put("pageSize",    pageSize);
        res.put("currentUserId", currentId);           // ★ 프런트에서 쓸 로그인 ID
        return res;
    }

    /* ───────────────────────────────────────────────
       2. 댓글 등록
    ─────────────────────────────────────────────── */
    @PostMapping("/board/replyInsert")
    @ResponseBody
    public Map<String,Object> insert(@RequestBody Comment c) throws Exception {
        Map<String,Object> res = new HashMap<>();
        LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
        if(!EgovUserDetailsHelper.isAuthenticated() || user==null){
            res.put("status","fail"); res.put("message","로그인이 필요합니다."); return res;
        }
        c.setFrstRegisterId(user.getUniqId());
        c.setWrterId(user.getUniqId());
        c.setWrterNm(user.getName());
        commentService.insertArticleComment(c);
        res.put("status","success"); res.put("message","댓글이 등록되었습니다.");
        return res;
    }

    /* ───────────────────────────────────────────────
       3. 댓글 수정 (본인만)
    ─────────────────────────────────────────────── */
    @PostMapping("/board/replyUpdate")
    @ResponseBody
    public Map<String,Object> update(@RequestBody Comment c) throws Exception {
        Map<String,Object> res = new HashMap<>();
        LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
        if(!EgovUserDetailsHelper.isAuthenticated() || user==null){
            res.put("status","fail"); res.put("message","로그인이 필요합니다."); return res;
        }
        CommentVO origin = commentService.selectArticleCommentDetail(
                             new CommentVO(){ { setCommentNo(c.getCommentNo()); } });
        if(origin==null || !user.getUniqId().equals(origin.getWrterId())){
            res.put("status","fail"); res.put("message","본인 댓글만 수정할 수 있습니다."); return res;
        }
        c.setLastUpdusrId(user.getUniqId());
        commentService.updateArticleComment(c);
        res.put("status","success"); res.put("message","댓글이 수정되었습니다.");
        return res;
    }

    /* ───────────────────────────────────────────────
       4. 댓글 삭제 (본인만)
    ─────────────────────────────────────────────── */
    @PostMapping("/board/replyDelete")
    @ResponseBody
    public Map<String,Object> delete(@RequestBody CommentVO vo) throws Exception {
        Map<String,Object> res = new HashMap<>();
        LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
        if(!EgovUserDetailsHelper.isAuthenticated() || user==null){
            res.put("status","fail"); res.put("message","로그인이 필요합니다."); return res;
        }
        CommentVO origin = commentService.selectArticleCommentDetail(vo);
        if(origin==null || !user.getUniqId().equals(origin.getWrterId())){
            res.put("status","fail"); res.put("message","본인 댓글만 삭제할 수 있습니다."); return res;
        }
        commentService.deleteArticleComment(vo);
        res.put("status","success"); res.put("message","댓글이 삭제되었습니다.");
        return res;
    }
}
