package org.takinframework.core.util;

import org.apache.commons.lang3.math.NumberUtils;

public class NumberUtil extends NumberUtils {
	
	public static Byte createByte(String str){
		if (str == null) {
            return null;
        }
        return Byte.valueOf(str);
		
	}
}
