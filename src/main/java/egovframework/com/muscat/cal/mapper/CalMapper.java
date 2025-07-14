	package egovframework.com.muscat.cal.mapper;
	
	import java.util.List;
	import java.util.Map;
	
	import org.apache.ibatis.annotations.Mapper;
	import org.apache.ibatis.annotations.Param;
	
	
	@Mapper
	public interface CalMapper {
	
	    void insertSchedule(ScudVO vo) throws Exception;
	
	    List<Map> selectScheduleList(@Param("ownerId") String ownerId, @Param("mberId") String mberId);
	    
	    void insertCalendar(CalendarVO calendarVO);
	
		List<Map> selectCalendarList(String loginId);
	
		String getNextScheduleId();
	
		List<Map<String, Object>> selectAvailableRooms(String reserveDateTime);
	
		void insertRoomReserve(ReservationVO reservationVO);
	
		List<Map> selectAllRooms();
		
		int updateSchedule(ScudVO vo);
		
		int deleteSchedule(String schdulId);
		
		ScudVO selectScheduleById(String schdulId);
		
		List<Map> selectCalendarsByUser(String loginId);

		void insertCalendarShare(CalendarShareVO vo);
		
		List<Map> selectSharedCalendars(String loginId);
		
		void updateCalendar(CalendarVO vo);
		void updateSharedCalendar(CalendarVO vo);
		
		int deleteCalendar(@Param("calId") String calId);
		int deleteSharedCalendar(@Param("calId") String calId);
	}
