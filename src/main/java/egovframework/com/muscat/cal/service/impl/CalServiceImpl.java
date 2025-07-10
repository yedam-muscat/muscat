package egovframework.com.muscat.cal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.cal.mapper.CalMapper;
import egovframework.com.muscat.cal.mapper.CalendarShareVO;
import egovframework.com.muscat.cal.mapper.CalendarVO;
import egovframework.com.muscat.cal.mapper.ReservationVO;
import egovframework.com.muscat.cal.mapper.ScudVO;
import egovframework.com.muscat.cal.service.CalService;

@Service
public class CalServiceImpl implements CalService {

    @Autowired
    private CalMapper calMapper;

    @Override
    public void insertSchedule(ScudVO vo) throws Exception {
        calMapper.insertSchedule(vo);
    }
 
    @Override
    public List<Map> selectScheduleList(String ownerId) throws Exception {
        return calMapper.selectScheduleList(ownerId);
    }
   

    @Override
    public void insertCalendar(CalendarVO calendarVO) {
        calMapper.insertCalendar(calendarVO);
    }
	@Override
	public List<Map> selectCalendarList(String loginId) {
		return calMapper.selectCalendarList(loginId);
	}
	
	@Override
	public List<Map<String, Object>> selectAvailableRooms(String reserveDateTime) {
	    return calMapper.selectAvailableRooms(reserveDateTime);
	}
	@Override
	public void insertRoomReserve(ReservationVO vo) throws Exception {
	    calMapper.insertRoomReserve(vo);
	}
	@Override
    public List<Map> getAllRooms() {
        return calMapper.selectAllRooms();
    }
	
	@Override
	public int updateSchedule(ScudVO vo) {
	    return calMapper.updateSchedule(vo);
	}

	@Override
    public int deleteSchedule(String schdulId) {
        return calMapper.deleteSchedule(schdulId);
    }
	
	@Override
	public ScudVO selectScheduleById(String schdulId) {
	    return calMapper.selectScheduleById(schdulId);
	}
	
	@Override
	public List<Map> selectCalendarListByUser(String loginId) {
	    return calMapper.selectCalendarsByUser(loginId);
	}
	
	@Override
	public void insertCalendarShare(CalendarShareVO vo) {
	    calMapper.insertCalendarShare(vo);
	}
	
	@Override
	public List<Map> getSharedCalendars(String loginId) {
	    return calMapper.selectSharedCalendars(loginId);
	}
	
}