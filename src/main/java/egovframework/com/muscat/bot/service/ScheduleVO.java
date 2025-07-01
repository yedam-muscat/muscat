package egovframework.com.muscat.bot.service;


import java.time.LocalDate;

public class ScheduleVO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;

    // 기본 생성자
    public ScheduleVO() {}

    // Getter & Setter
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
