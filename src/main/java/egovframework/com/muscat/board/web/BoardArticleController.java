package egovframework.com.muscat.board.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.*;

@Controller
public class BoardArticleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardArticleController.class);

    @Resource(name = "EgovArticleService")
    private EgovArticleService egovArticleService;

    @Resource(name = "EgovBBSMasterService")
    private EgovBBSMasterService egovBBSMasterService;

    @Resource(name = "EgovFileMngService")
    private EgovFileMngService fileMngService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    @Autowired
    private DefaultBeanValidator beanValidator;

    /** XSS 방지 필터링 */
    protected String unscript(String data) {
        if (data == null || data.trim().isEmpty()) return "";
        return data.replaceAll("(?i)<script", "&lt;script")
                   .replaceAll("(?i)</script", "&lt;/script")
                   .replaceAll("(?i)<object", "&lt;object")
                   .replaceAll("(?i)</object", "&lt;/object")
                   .replaceAll("(?i)<applet", "&lt;applet")
                   .replaceAll("(?i)</applet", "&lt;/applet")
                   .replaceAll("(?i)<embed", "&lt;embed")
                   .replaceAll("(?i)</embed", "&lt;/embed")
                   .replaceAll("(?i)<form", "&lt;form")
                   .replaceAll("(?i)</form", "&lt;/form");
    }

    /** 📄 게시글 목록 페이지 이동 */
    @GetMapping("/board/articleList.do")
    public String articleListPage(@RequestParam("bbsId") String bbsId, Model model) throws Exception {
        BoardMasterVO boardInfo = new BoardMasterVO();
        boardInfo.setBbsId(bbsId);

        model.addAttribute("bbsId", bbsId);
        model.addAttribute("boardInfo", egovBBSMasterService.selectBBSMasterInf(boardInfo));
        return "board/articleList.html";
    }

    /** 📑 게시글 목록 데이터 (JSON) */
    @GetMapping("/board/articleList")
    @ResponseBody
    public Map<String, Object> articleList(@ModelAttribute("searchVO") BoardVO boardVO,
                                           @RequestParam("bbsId") String bbsId) throws Exception {
        Map<String, Object> result = new HashMap<>();

        if (!EgovUserDetailsHelper.isAuthenticated()) {
            result.put("resultCode", "FAIL");
            result.put("resultMessage", "로그인이 필요합니다.");
            return result;
        }

        boardVO.setBbsId(bbsId);
        boardVO.setPageUnit(propertyService.getInt("pageUnit"));
        boardVO.setPageSize(propertyService.getInt("pageSize"));

        PaginationInfo pageInfo = new PaginationInfo();
        pageInfo.setCurrentPageNo(boardVO.getPageIndex());
        pageInfo.setRecordCountPerPage(boardVO.getPageUnit());
        pageInfo.setPageSize(boardVO.getPageSize());

        boardVO.setFirstIndex(pageInfo.getFirstRecordIndex());
        boardVO.setLastIndex(pageInfo.getLastRecordIndex());
        boardVO.setRecordCountPerPage(pageInfo.getRecordCountPerPage());

        Map<String, Object> dataMap = egovArticleService.selectArticleList(boardVO);
        int totalCount = Integer.parseInt((String) dataMap.get("resultCnt"));

        pageInfo.setTotalRecordCount(totalCount);

        result.put("resultCode", "SUCCESS");
        result.put("resultList", dataMap.get("resultList"));
        result.put("resultCnt", dataMap.get("resultCnt"));
        result.put("pageIndex", boardVO.getPageIndex());
        result.put("pageUnit", boardVO.getPageUnit());
        result.put("totalPageCount", pageInfo.getTotalPageCount());

        return result;
    }

    /** 📄 게시글 상세 페이지 */
    @GetMapping("/board/articleDetail.do")
    public String articleDetailPage(@RequestParam("bbsId") String bbsId,
                                     @RequestParam("nttId") int nttId,
                                     Model model) {
        model.addAttribute("bbsId", bbsId);
        model.addAttribute("nttId", nttId);
        return "board/articleDetail.html";
    }

    /** 📑 게시글 상세 정보 (JSON) */
    @GetMapping("/board/articleDetail")
    @ResponseBody
    public Map<String, Object> articleDetail(@RequestParam("bbsId") String bbsId,
                                             @RequestParam("nttId") int nttId) {
        Map<String, Object> result = new HashMap<>();
        try {
            BoardVO vo = new BoardVO();
            vo.setBbsId(bbsId);
            vo.setNttId(nttId);

            result.put("resultCode", "SUCCESS");
            result.put("article", egovArticleService.selectArticleDetail(vo));
        } catch (Exception e) {
            result.put("resultCode", "FAIL");
            result.put("resultMessage", "오류 발생: " + e.getMessage());
        }
        return result;
    }

    /** 📝 게시글 등록 화면 */
    @GetMapping("/board/articleRegist.do")
    public String articleRegistPage(@RequestParam("bbsId") String bbsId, Model model) {
        if (!EgovUserDetailsHelper.isAuthenticated()) {
            return "redirect:/uat/uia/egovLoginUsr.do";
        }

        LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

        BoardMasterVO boardMaster = new BoardMasterVO();
        boardMaster.setBbsId(bbsId);
        boardMaster.setUniqId(user.getUniqId());

        try {
            boardMaster = egovBBSMasterService.selectBBSMasterInf(boardMaster);
        } catch (Exception e) {
            LOGGER.error("게시판 정보 조회 실패", e);
        }

        if (boardMaster.getTmplatCours() == null || boardMaster.getTmplatCours().isEmpty()) {
            boardMaster.setTmplatCours("/css/egovframework/com/cop/tpl/egovBaseTemplate.css");
        }

        model.addAttribute("boardMasterVO", boardMaster);
        return "board/articleRegist.html";
    }

    /** ✅ 게시글 등록 처리 */
    @PostMapping("/board/articleInsert.do")
    public String insertArticle(final MultipartHttpServletRequest multiRequest,
                                 @ModelAttribute("searchVO") BoardVO boardVO,
                                 @ModelAttribute("bdMstr") BoardMaster bdMstr,
                                 @ModelAttribute("board") BoardVO board,
                                 BindingResult bindingResult, ModelMap model) throws Exception {

        if (!EgovUserDetailsHelper.isAuthenticated()) {
            return "redirect:/uat/uia/egovLoginUsr.do";
        }
        LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

        beanValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            BoardMasterVO masterVO = new BoardMasterVO();
            masterVO.setBbsId(boardVO.getBbsId());
            masterVO.setUniqId(user.getUniqId());
            BoardMasterVO master = egovBBSMasterService.selectBBSMasterInf(masterVO);

            if (master.getTmplatCours() == null || master.getTmplatCours().isEmpty()) {
                master.setTmplatCours("/css/egovframework/com/cop/tpl/egovBaseTemplate.css");
            }
            model.addAttribute("boardMasterVO", master);
            return "egovframework/com/cop/bbs/EgovArticleRegist";
        }

        board.setBbsId(boardVO.getBbsId());
        board.setFrstRegisterId(user.getUniqId());

        if ("Y".equals(board.getAnonymousAt())) {
            board.setNtcrId("anonymous");
            board.setNtcrNm("익명");
            board.setFrstRegisterId("anonymous");
        } else {
            board.setNtcrId(user.getUniqId());
            board.setNtcrNm(user.getName());
        }

        board.setNttCn(unscript(board.getNttCn()));

        List<MultipartFile> files = multiRequest.getFiles("file_1");
        egovArticleService.insertArticleAndFiles(board, files);

        model.addAttribute("bbsId", boardVO.getBbsId());
        model.addAttribute("searchCnd", boardVO.getSearchCnd());
        model.addAttribute("searchWrd", boardVO.getSearchWrd());
        model.addAttribute("pageIndex", boardVO.getPageIndex());

        return "redirect:articleList.do";
    }

    /** ✏️ 게시글 수정 화면 */
    @GetMapping("/board/articleUpdt.do")
    public String updateArticleView(@RequestParam("bbsId") String bbsId,
                                     @RequestParam("nttId") int nttId,
                                     ModelMap model) throws Exception {

        LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

        BoardVO boardVO = new BoardVO();
        boardVO.setBbsId(bbsId);
        boardVO.setNttId(nttId);

        BoardVO article = egovArticleService.selectArticleDetail(boardVO);

        BoardMasterVO boardMasterVO = new BoardMasterVO();
        boardMasterVO.setBbsId(bbsId);
        boardMasterVO.setUniqId(user != null ? user.getUniqId() : "");

        BoardMasterVO boardMaster = egovBBSMasterService.selectBBSMasterInf(boardMasterVO);

        if ("anonymous".equals(article.getNtcrId())) {
            model.addAttribute("result", article);
            model.addAttribute("boardMasterVO", boardMaster);
            return "board/articleDetail.html";
        }

        model.addAttribute("articleVO", article);
        model.addAttribute("boardMasterVO", boardMaster);
        return "board/articleUpdt.html";
    }

    /** ✅ 게시글 수정 처리 (AJAX) */
    @PostMapping("/board/articleUpdate")
    @ResponseBody
    public Map<String, Object> updateArticle(MultipartHttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
            if (user == null || user.getUniqId() == null) {
                resultMap.put("resultCode", "FAIL");
                resultMap.put("resultMessage", "로그인이 필요합니다.");
                return resultMap;
            }

            BoardVO board = new BoardVO();
            board.setBbsId(request.getParameter("bbsId"));
            board.setNttId(Integer.parseInt(request.getParameter("nttId")));
            board.setNttSj(request.getParameter("nttSj"));
            board.setNttCn(unscript(request.getParameter("nttCn")));
            board.setNoticeAt(request.getParameter("noticeAt") != null ? "Y" : "N");
            board.setSecretAt(request.getParameter("secretAt") != null ? "Y" : "N");
            board.setAnonymousAt(request.getParameter("anonymousAt") != null ? "Y" : "N");
            board.setSjBoldAt(request.getParameter("sjBoldAt") != null ? "Y" : "N");
            board.setLastUpdusrId(user.getUniqId());

            List<MultipartFile> files = request.getFiles("file");

            BoardVO origin = egovArticleService.selectArticleDetail(board);
            board.setAtchFileId(origin.getAtchFileId());

            egovArticleService.updateArticleAndFiles(board, files, board.getAtchFileId());

            resultMap.put("resultCode", "SUCCESS");
            resultMap.put("resultMessage", "게시글이 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            resultMap.put("resultCode", "FAIL");
            resultMap.put("resultMessage", "게시글 수정 중 오류가 발생했습니다.");
            LOGGER.error("게시글 수정 오류", e);
        }

        return resultMap;
    }
}
