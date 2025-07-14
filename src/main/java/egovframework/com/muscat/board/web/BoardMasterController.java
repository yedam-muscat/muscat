package egovframework.com.muscat.board.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import com.fasterxml.jackson.databind.ObjectMapper;                 // ★ JSON 직렬화

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.cop.bbs.service.BoardMaster;
import egovframework.com.cop.bbs.service.BoardMasterVO;
import egovframework.com.cop.bbs.service.EgovBBSMasterService;

/* ▼ 조직도 */
import egovframework.com.muscat.group.service.GroupService;
import egovframework.com.muscat.group.service.GroupVO;

@Controller
public class BoardMasterController {

    /* ───── 서비스 주입 ───── */
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

    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    @Autowired private DefaultBeanValidator beanValidator;
    @Autowired private GroupService groupService;

    /* ===================================================================== */
    /* 공통: 부서 데이터(model) 주입                                          */
    /* ===================================================================== */
    private void addDeptAttrs(Model model) throws Exception {
        List<GroupVO> all = groupService.getGroupChartData();

        List<GroupVO> parents = all.stream()
                                   .filter(d -> "#".equals(d.getParent()))
                                   .collect(Collectors.toList());

        List<GroupVO> childs  = all.stream()
                                   .filter(d -> !"#".equals(d.getParent()))
                                   .collect(Collectors.toList());

        model.addAttribute("parent_dept_result", parents);
        model.addAttribute("child_dept_result",  childs);
        
        /* ★ 화면의 <option th:each="d : ${dept_result}"> 용 전체 리스트 */
        model.addAttribute("dept_result", all);   // ← 이 한 줄만 추가!

        /* JS에서 안전히 쓰고 싶다면 ↓ 직렬화 */
        String childJson = new ObjectMapper().writeValueAsString(childs);
        model.addAttribute("child_dept_json", childJson);
    }

    /* ───────────────────────── 목록 화면 / JSON ───────────────────────── */

    @GetMapping("/board/masterList.do")
    public String masterList() { return "board/masterList.html"; }

    @GetMapping("/board/masterList")
    @ResponseBody
    public Map<String, Object> boardMaster(
            @RequestParam(defaultValue="")  String searchCnd,
            @RequestParam(defaultValue="")  String searchWrd,
            @RequestParam(defaultValue="1") int pageIndex,
            @RequestParam(required=false)  Integer pageSize) throws Exception {

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

        return egovBBSMasterService.selectBBSMasterInfs(vo);
    }

    /* ───────────────────────── 게시판 등록 ───────────────────────── */

    @GetMapping("/board/masterRegist.do")
    public String masterRegist(Model model) throws Exception {

        /* ① 공통코드(직급 등) */
        ComDefaultCodeVO vo = new ComDefaultCodeVO();
        vo.setCodeId("COM103");
        model.addAttribute("deptList", cmmUseService.selectCmmCodeDetail(vo));

        /* ② 부서 데이터 */
        addDeptAttrs(model);

        return "board/masterRegist.html";
    }

    @PostMapping("/board/masterInsert")
    @ResponseBody
    public Map<String, Object> insertBoardMaster(@RequestBody BoardMaster boardMaster) {
        Map<String, Object> r = new HashMap<>();
        try {
            if (!EgovUserDetailsHelper.isAuthenticated()) {
                r.put("resultCode","FAIL"); r.put("resultMessage","로그인 필요"); return r;
            }
            LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
            boardMaster.setFrstRegisterId(user!=null?user.getUniqId():"");
            boardMaster.setBlogAt("Y".equals(boardMaster.getBlogAt())?"Y":"N");

            egovBBSMasterService.insertBBSMasterInf(boardMaster);
            r.put("resultCode","SUCCESS"); r.put("resultMessage","등록 완료");
        } catch(Exception e){
            r.put("resultCode","FAIL"); r.put("resultMessage","오류: "+e.getMessage());
        }
        return r;
    }

    /* ───────────────────────── 게시판 상세 / 삭제 ───────────────────────── */

    @GetMapping("/board/masterDetail.do")
    public String masterDetailPage() { return "board/masterDetail.html"; }

    @GetMapping("/board/masterDetail")
    @ResponseBody
    public BoardMasterVO getBoardMasterDetail(@RequestParam("bbsId") String bbsId) throws Exception {
        BoardMasterVO vo = new BoardMasterVO(); vo.setBbsId(bbsId);
        return egovBBSMasterService.selectBBSMasterInf(vo);
    }

    @PostMapping("/board/masterDelete")
    @ResponseBody
    public Map<String,Object> deleteBoardMaster(@RequestBody BoardMasterVO vo){
        Map<String,Object> r=new HashMap<>();
        try{
            egovBBSMasterService.deleteBBSMasterInf(vo);
            r.put("resultCode","SUCCESS"); r.put("resultMessage","삭제 완료");
        }catch(Exception e){
            r.put("resultCode","FAIL"); r.put("resultMessage","삭제 실패: "+e.getMessage());
        }
        return r;
    }

    /* ───────────────────────── 게시판 수정 ───────────────────────── */

    @GetMapping("/board/masterUpdt.do")
    public String masterEditPage(Model model) throws Exception {

        ComDefaultCodeVO vo = new ComDefaultCodeVO();
        vo.setCodeId("COM103");
        model.addAttribute("deptList", cmmUseService.selectCmmCodeDetail(vo));

        addDeptAttrs(model);      // 부서 주입 (중복 제거)

        return "board/masterUpdt.html";
    }

    @PostMapping("/board/masterUpdt")
    @ResponseBody
    public Map<String,Object> updateBoardMaster(@RequestBody BoardMaster boardMaster){
        Map<String,Object> r=new HashMap<>();
        try{
            if(!EgovUserDetailsHelper.isAuthenticated()){
                r.put("resultCode","FAIL"); r.put("resultMessage","로그인 필요"); return r;
            }
            if(boardMaster.getBbsId()==null||boardMaster.getBbsId().isEmpty()){
                r.put("resultCode","FAIL"); r.put("resultMessage","BBS ID 없음"); return r;
            }
            LoginVO user=(LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
            boardMaster.setLastUpdusrId(user!=null?user.getUniqId():"");
            if(boardMaster.getOption()==null||boardMaster.getOption().isEmpty()){
                boardMaster.setOption("na");
            }
            egovBBSMasterService.updateBBSMasterInf(boardMaster);
            r.put("resultCode","SUCCESS"); r.put("resultMessage","수정 완료");
        }catch(Exception e){
            r.put("resultCode","FAIL"); r.put("resultMessage","오류: "+e.getMessage());
        }
        return r;
    }

    /* ───────────────────────── 공통상세코드 조회 ───────────────────────── */

    @GetMapping("/board/commonCodes")
    @ResponseBody
    public List<Map<String,String>> getCommonCodes(@RequestParam("groupCode") String groupCode) throws Exception {
        ComDefaultCodeVO vo = new ComDefaultCodeVO(); vo.setCodeId(groupCode);
        List<CmmnDetailCode> list = cmmUseService.selectCmmCodeDetail(vo);
        List<Map<String,String>> r = new ArrayList<>();
        for(CmmnDetailCode c : list){
            Map<String,String> m = new HashMap<>();
            m.put("code",c.getCode()); m.put("codeNm",c.getCodeNm()); r.add(m);
        }
        return r;
    }
}
