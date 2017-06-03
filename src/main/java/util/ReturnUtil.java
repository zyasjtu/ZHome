package util;

import com.alibaba.fastjson.JSON;
import constant.CodeConstant;
import constant.MsgConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Colin on 2017/6/4.
 */
public class ReturnUtil {
    public static Map<String, Object> buildReturnMap(String respCode, String respMsg) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put(CodeConstant.KEY, respCode);
        returnMap.put(MsgConstant.KEY, respMsg + "[" + respCode + "]");
        return returnMap;
    }

    public static String buildReturnJson(String respCode, String respMsg) {
        return JSON.toJSONString(buildReturnMap(respCode, respMsg));
    }
}
