package test;

import com.vuclip.abtesthttp.util.DateUtil;
import com.vuclip.abtesthttp.util.Pagination;


/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/19
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
public class TestMain extends OverwrideClass{
	
	
	public static void main(String[] args){
		String star = "20150520";
		System.out.println(star.substring(0,4)+star.substring(4,6)+star.substring(6,8));
		new TestMain().testOverWride();
	}
        /*System.out.println(CommonUtils.string2MD5("vuclip"));
        System.out.println(CommonUtils.convertMD5(CommonUtils.convertMD5(CommonUtils.string2MD5("vuclip"))));*/

	public void testOverLoad(){
		System.out.println("method1..");
	}
	public void testOverLoad(String aa){
		System.out.println("method1..");
	}

	@Override
	public void testOverWride() {
		super.testOverWride();
		System.out.println("override method...");
	}
}
