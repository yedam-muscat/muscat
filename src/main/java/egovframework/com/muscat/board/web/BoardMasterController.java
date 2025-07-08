package egovframework.com.muscat.board.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;            // ✚
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.BoardMaster;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;

@Controller
public class BoardMasterController {

    @Resource(name = "EgovBBSMasterService")
    private EgovBBSMasterService egovBBSMasterService;

    @Resource(name = "EgovCmmUseService")
    private EgovCmmUseService cmmUseService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "egovBBSMstrIdGnrService")
    private EgovIdGnrService idgenServiceBbs;

    @Resource(name = "egovBlogIdGnrService")
    private EgovIdGnrService idgenServiceBlog;

    /** EgovMessageSource */
    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    @Autowired
    private DefaultBeanValidator beanValidator;

    /* ───────────────────────────────────────── 목록 화면 / JSON ───────────────────────────────────────── */

    /** 📄 목록 화면 */
    @GetMapping("/board/masterList.do")
    public String masterList() {
        return "board/masterList.html";
    }

    /** 📑 목록 JSON  (검색 & 페이지) */
    @GetMapping("/board/masterList")
    @ResponseBody
    public Map<String, Object> boardMaster(
            @RequestParam(defaultValue = "")  String searchCnd,
            @RequestParam(defaultValue = "")  String searchWrd,
            @RequestParam(defaultValue = "1") int    pageIndex,
            @RequestParam(required   = false) Integer pageSize) throws Exception {

        BoardMasterVO vo = new BoardMasterVO();
        vo.setSearchCnd(searchCnd);
        vo.setSearchWrd(searchWrd);
        vo.setPageIndex(pageIndex);

        vo.setPageUnit(propertyService.getInt("pageUnit"));
        vo.setPageSize(pageSize != null ? pageSize : propertyService.getInt("pageSize"));

        PaginationInfo pi = new PaginationInfo();
        pi.setCurrentPageNo(vo.getPageIndex());
        pi.setRecordCountPerPage(vo.getPageUnit());
        pi.setPageSize(vo.getPageSize());

        vo.setFirstIndex(pi.getFirstRecordIndex());
        vo.setRecordCountPerPage(pi.getRecordCountPerPage());

        return egovBBSMasterService.selectBBSMasterInfs(vo); // resultList / resultCnt 포함
    }

    /* ───────────────────────────────────────── 게시판 등록 ───────────────────────────────────────── */

    /** 📄 등록 페이지  ✚ deptList 모델 주입 */
    @GetMapping("/board/masterRegist.do")
    public String masterRegist(Model model) throws Exception {          // ✚
        ComDefaultCodeVO vo = new ComDefaultCodeVO();                   // ✚
        vo.setCodeId("COM103");                                         // 부서코드 그룹ID
        List<CmmnDetailCode> deptList = cmmUseService.selectCmmCodeDetail(vo);
        model.addAttribute("deptList", deptList);                       // ✚
        return "board/masterRegist.html";
    }

    /** ✅ 등록 처리 */
    @PostMapping("/board/masterInsert")
    @ResponseBody
    public Map<String, Object> insertBoardMaster(@RequestBody BoardMaster boardMaster) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
            if (!EgovUserDetailsHelper.isAuthenticated()) {
                resultMap.put("resultCode", "FAIL");
                resultMap.put("resultMessage", "로그인이 필요합니다.");
                return resultMap;
            }

            boardMaster.setFrstRegisterId(user != null ? user.getUniqId() : "");

            /* 옵션값 보정 */
            boardMaster.setBlogAt("Y".equals(boardMaster.getBlogAt()) ? "Y" : "N");

            egovBBSMasterService.insertBBSMasterInf(boardMaster);

            resultMap.put("resultCode", "SUCCESS");
            resultMap.put("resultMessage", "등록이 완료되었습니다.");
        } catch (Exception e) {
            resultMap.put("resultCode", "FAIL");
            resultMap.put("resultMessage", "오류 발생: " + e.getMessage());
        }
        return resultMap;
    }

    /* ───────────────────────────────────────── 게시판 상세 / 삭제 ───────────────────────────────────────── */

    @GetMapping("/board/masterDetail.do")
    public String masterDetailPage() {
        return "board/masterDetail.html";
    }

    @GetMapping("/board/masterDetail")
    @ResponseBody
    public BoardMasterVO getBoardMasterDetail(@RequestParam("bbsId") String bbsId) throws Exception {
        BoardMasterVO searchVO = new BoardMasterVO();
        searchVO.setBbsId(bbsId);
        return egovBBSMasterService.selectBBSMasterInf(searchVO);
    }

    @PostMapping("/board/masterDelete")
    @ResponseBody
    public Map<String, Object> deleteBoardMaster(@RequestBody BoardMasterVO vo) {
        Map<String, Object> result = new HashMap<>();
        try {
            egovBBSMasterService.deleteBBSMasterInf(vo);
            result.put("resultCode", "SUCCESS");
            result.put("resultMessage", "삭제되었습니다.");
        } catch (Exception e) {
            result.put("resultCode", "FAIL");
            result.put("resultMessage", "삭제 실패: " + e.getMessage());
        }
        return result;
    }

    /* ───────────────────────────────────────── 게시판 수정 ───────────────────────────────────────── */

    /** 📄 수정 페이지  ✚ deptList 재사용 */
    @GetMapping("/board/masterUpdt.do")
    public String masterEditPage(Model model) throws Exception {        // ✚
        ComDefaultCodeVO vo = new ComDefaultCodeVO();                   // ✚
        vo.setCodeId("COM103");
        model.addAttribute("deptList", cmmUseService.selectCmmCodeDetail(vo));
        return "board/masterUpdt.html";
    }

    /** ✅ 수정 처리 */
    @PostMapping("/board/masterUpdt")
    @ResponseBody
    public Map<String, Object> updateBoardMaster(@RequestBody BoardMaster boardMaster) {
        Map<String, Object> result = new HashMap<>();
        try {
            LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
            if (!EgovUserDetailsHelper.isAuthenticated()) {
                result.put("resultCode", "FAIL");
                result.put("resultMessage", "로그인이 필요합니다.");
                return result;
            }

            if (boardMaster.getBbsId() == null || boardMaster.getBbsId().isEmpty()) {
                result.put("resultCode", "FAIL");
                result.put("resultMessage", "게시판 ID가 없습니다.");
                return result;
            }

            boardMaster.setLastUpdusrId(user != null ? user.getUniqId() : "");

            if (boardMaster.getOption() == null || boardMaster.getOption().isEmpty()) {
                boardMaster.setOption("na");
            }

            egovBBSMasterService.updateBBSMasterInf(boardMaster);

            result.put("resultCode", "SUCCESS");
            result.put("resultMessage", "수정이 완료되었습니다.");
        } catch (Exception e) {
            result.put("resultCode", "FAIL");
            result.put("resultMessage", "오류 발생: " + e.getMessage());
        }
        return result;
    }

    /* ───────────────────────────────────────── 공통상세코드 조회 ───────────────────────────────────────── */

    @GetMapping("/board/commonCodes")
    @ResponseBody
    public List<Map<String, String>> getCommonCodes(@RequestParam("groupCode") String groupCode) throws Exception {
        ComDefaultCodeVO vo = new ComDefaultCodeVO();
        vo.setCodeId(groupCode);

        List<CmmnDetailCode> codeList = cmmUseService.selectCmmCodeDetail(vo);
        List<Map<String, String>> result = new ArrayList<>();

        for (CmmnDetailCode c : codeList) {
            Map<String, String> m = new HashMap<>();
            m.put("code",   c.getCode());
            m.put("codeNm", c.getCodeNm());
            result.add(m);
        }
        return result;
    }
}
