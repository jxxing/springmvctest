package jxx;

import org.junit.Test;

import com.rd.ifaes.common.util.HttpUtils;

public class HttpUtilTest {

	@Test
	public void test(){
		String url = "https://www.wjs.com/web/chat/getEncryptInfo";
		
//		String[][] strss = {{"aa","bb"},{"aa","bb"}};
		String[][] strss = {{"isSelectedTip","1"}};
		

		String resp = HttpUtils.postClient(url, strss);
		
		
		System.out.println(resp);
	}
}
