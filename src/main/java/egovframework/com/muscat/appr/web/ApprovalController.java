

package egovframework.com.muscat.appr.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// import com.yedam.muscat.approval.service.ApprovalFormService;
// import com.yedam.muscat.approval.service.ElectronicApprovalForm;

@Controller
public class ApprovalController {

    // @Autowired
    // private ApprovalFormService approvalFormService; // 실제로는 서비스와 매퍼를 구현해야 합니다.

    /**
     * 관리자용 양식 생성기 페이지로 이동합니다.
     */
    @GetMapping("/approval/builder.do")
    public String showFormBuilder() {
        return "approval/form_builder.html";
    }

    /**
     * 사용자가 결재 문서를 작성할 수 있도록 양식을 렌더링합니다.
     * @param formId DB에 저장된 양식의 ID
     * @param model Thymeleaf 모델
     * @return 렌더링할 뷰 이름
     */
    @GetMapping("/approval/form.do")
    public String renderApprovalForm(int formId, Model model) {
        // --- 실제 구현에서는 아래 로직이 필요합니다 ---
        // 1. formId를 사용하여 DB에서 양식 정보를 조회합니다.
        // ElectronicApprovalForm form = approvalFormService.getFormById(formId);

        // --- 테스트를 위한 더미 데이터 ---
        // 실제로는 위에서 주석 처리된 코드를 통해 DB에서 가져와야 합니다.
        String dummySchema = "{\"components\":[{\"type\":\"textfield\",\"key\":\"firstName\",\"label\":\"이름\"},{\"type\":\"textarea\",\"key\":\"content\",\"label\":\"내용\"},{\"type\":\"button\",\"label\":\"제출\",\"key\":\"submit\",\"disableOnInvalid\":true,\"input\":true,\"tableView\":false}]}";
        String formName = "테스트 양식";
        // --- 더미 데이터 끝 ---

        // 모델에 양식 스키마와 이름을 추가하여 뷰로 전달합니다.
        // model.addAttribute("formSchema", form.getFormSchema());
        // model.addAttribute("formName", form.getFormName());
        model.addAttribute("formSchema", dummySchema);
        model.addAttribute("formName", formName);

        return "approval/form_renderer.html";
    }
}
