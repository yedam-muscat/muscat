package egovframework.com.muscat.admin.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	
	public int checkUserAuth(String uniqId);

}
