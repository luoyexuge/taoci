package com.iclick.spark;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Test {
	public static void main(String[] args) {
		System.out.println(checkIDCard("36042419900264287x"));
		
		
	}

	public static boolean checkIDCard(String str) {
		boolean  boo=true;
		if (str == null) {
			boo=false;
		} else {
			Pattern  pattern = Pattern.compile(
					"^[1-9]\\d{5}[1-9]\\d{3}((0\\d{1})|(1[0-2]{1}))(([0|1|2]\\d{1})|(3[0-1]{1}))\\d{3}[0-9xX]{1}");
					
			Matcher result = pattern.matcher(str);
			if(result.find()){
				System.out.println(result.group(1));
				System.out.println(result.group(4));
				String monthday=result.group(1)+result.group(4);
				if("0230".equals(monthday)||"0231".equals(monthday)){
					boo=false;
				}
			}else{
				boo=false;
			}

		}
		return  boo;
	}

}
