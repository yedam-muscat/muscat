package egovframework.com.muscat.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVO {

	private boolean resultSuccess;
	private String resultCode;
	private String resultMsg;
	
}
