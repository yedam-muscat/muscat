package egovframework.com.muscat.cal.service;

import java.util.List;
import java.util.Map;

import egovframework.com.muscat.cal.mapper.CalendarVO;
import egovframework.com.muscat.cal.mapper.ReservationVO;
import egovframework.com.muscat.cal.mapper.ScudVO;

public interface CalService {
	
	void insertSchedule(ScudVO vo) throws Exception;
	
	List<Map> selectScheduleList() throws Exception;
	
	 void insertCalendar(CalendarVO calendarVO);

	List<Map> selectCalendarList();

	List<Map<String, Object>> selectAvailableRooms(String reserveDateTime);

	void insertRoomReserve(ReservationVO vo) throws Exception;
}
