package egovframework.com.muscat.bot.web;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.muscat.bot.service.ScheduleVO;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    // 임시 저장용 메모리 리스트 (DB 대신)
    private final List<ScheduleVO> schedules = new ArrayList<>();

    @PostMapping("/add")
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleVO schedule) {
        // 간단 유효성 체크
        if (schedule.getStartDate() == null || schedule.getEndDate() == null || schedule.getTitle() == null || schedule.getTitle().isBlank()) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(false, "일정 정보가 올바르지 않습니다.")
            );
        }

        // 일정 저장 (실제로는 DB 저장)
        schedules.add(schedule);

        return ResponseEntity.ok(new ApiResponse(true, "일정이 성공적으로 추가되었습니다."));
    }

    // ApiResponse 내부 클래스 (간단 응답 포맷)
    static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}