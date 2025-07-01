package egovframework.com.muscat.group.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.muscat.group.service.GroupService;
import egovframework.com.muscat.group.service.GroupVO;
import egovframework.com.muscat.group.service.GroupwithVO;
import egovframework.com.uss.umt.service.EgovMberManageService;
import egovframework.com.uss.umt.service.MberManageVO;
import egovframework.com.uss.umt.service.UserDefaultVO;

@Controller
@RequestMapping("/group")
public class GroupController {
	
	 /** mberManageService */
    @Resource(name = "mberManageService")
    private EgovMberManageService mberManageService;

    /** cmmUseService */
    @Resource(name = "EgovCmmUseService")
    private EgovCmmUseService cmmUseService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** DefaultBeanValidator beanValidator */
    @Autowired
    private DefaultBeanValidator beanValidator;

    /**
     * 일반회원목록을 조회한다. (pageing)
     *
     * @param userSearchVO 검색조건정보
     * @param model        화면모델
     * @return uss/umt/EgovMberManage
     * @throws Exception
     */
	
 @Autowired GroupService groupService;
 
 @GetMapping("/chartData.do")
 @ResponseBody
 public List<GroupVO> ChartData() {
     return groupService.getGroupChartData();
 }
 
 @GetMapping("/groupData.do")
 @ResponseBody
 public List<GroupwithVO> GroupData() {
	 return groupService.getGroupData();
 }
 
//user 목록 조회
	@GetMapping("/admin/user/userList.do")
	@ResponseBody
	public List<MberManageVO> userList(@ModelAttribute("userSearchVO") UserDefaultVO userSearchVO)
			throws Exception {
		
      /** EgovPropertyService */
      userSearchVO.setPageUnit(propertiesService.getInt("pageUnit"));
      userSearchVO.setPageSize(propertiesService.getInt("pageSize"));

      /** pageing */
      PaginationInfo paginationInfo = new PaginationInfo();
      paginationInfo.setCurrentPageNo(userSearchVO.getPageIndex());
      paginationInfo.setRecordCountPerPage(userSearchVO.getPageUnit());
      paginationInfo.setPageSize(userSearchVO.getPageSize());

      userSearchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
      userSearchVO.setLastIndex(paginationInfo.getLastRecordIndex());
      userSearchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

      List<MberManageVO> resultList = mberManageService.selectMberList(userSearchVO);
      
		return resultList;
	}
	
	// user 상세 조회

	@GetMapping("/admin/user/userDetail.do")
	public String userDetail(@RequestParam("selectedId") String mberId, @ModelAttribute("searchVO") UserDefaultVO userSearchVO, HttpServletRequest request, Model model) throws Exception {

      ComDefaultCodeVO vo = new ComDefaultCodeVO();

      // 패스워드힌트목록을 코드정보로부터 조회
      vo.setCodeId("COM022");
      List<CmmnDetailCode> passwordHint_result = cmmUseService.selectCmmCodeDetail(vo);

      // 성별구분코드를 코드정보로부터 조회
      vo.setCodeId("COM014");
      List<CmmnDetailCode> sexdstnCode_result = cmmUseService.selectCmmCodeDetail(vo);

      // 사용자상태코드를 코드정보로부터 조회
      vo.setCodeId("COM013");
      List<CmmnDetailCode> mberSttus_result = cmmUseService.selectCmmCodeDetail(vo);

      // 그룹정보를 조회 - GROUP_ID정보
      vo.setTableNm("COMTNORGNZTINFO");
      List<CmmnDetailCode> groupId_result = cmmUseService.selectGroupIdDetail(vo);

      model.addAttribute("passwordHint_result", passwordHint_result); // 패스워트힌트목록
      model.addAttribute("sexdstnCode_result", sexdstnCode_result); // 성별구분코드목록
      model.addAttribute("mberSttus_result", mberSttus_result); // 사용자상태코드목록
      model.addAttribute("groupId_result", groupId_result); // 그룹정보 목록

      MberManageVO mberManageVO = mberManageService.selectMber(mberId);
      model.addAttribute("mberManageVO", mberManageVO);
      model.addAttribute("userSearchVO", userSearchVO);

      return "admin/userDetail.html";
  }
	
	
 
}
