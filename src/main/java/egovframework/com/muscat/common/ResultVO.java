package egovframework.com.muscat.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVO {

	public ResultVO() {
		// TODO Auto-generated constructor stub
	}
	private boolean resultSuccess;
	private String resultCode;
	private String resultMsg;
	
}
