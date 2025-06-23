package egovframework.com.muscat.cal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.muscat.cal.mapper.CalMapper;
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
    public List<Map> selectScheduleList() throws Exception {
        return calMapper.selectScheduleList();
    }
}