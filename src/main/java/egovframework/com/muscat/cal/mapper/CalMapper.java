package egovframework.com.muscat.cal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CalMapper {

    void insertSchedule(ScudVO vo) throws Exception;

    List<Map> selectScheduleList() throws Exception;
    
    void insertCalendar(CalendarVO calendarVO);

	List<Map> selectCalendarList();
}
