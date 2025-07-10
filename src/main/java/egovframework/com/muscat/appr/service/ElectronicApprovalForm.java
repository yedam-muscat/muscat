
package egovframework.com.muscat.appr.service;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ElectronicApprovalForm {

    private Long formId;
    private String formName;
    private String formDesc;
    private String formSchema; // JSON a string
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
