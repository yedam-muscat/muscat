package egovframework.com.muscat.main.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

	String selectBssId();
}
