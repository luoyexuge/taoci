package com.iclick.featureselect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class Information {
	public static Map<String, Double> caculateinfo(ArrayList<List<String>> list)
			throws IOException {
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<String, Double> result = new HashMap<String, Double>();

		map.put("1", 0);
		map.put("0", 0);
		for (List<String> li : list) {
			int size = li.size();

			String target = Integer.parseInt(li.get(10))==1 ? "1"
					: "0";
			if (map.containsKey(target)) {
				map.put(target, map.get(target) + 1);
			} else {

				map.put(target, 1);
			}
		}

		int sum = map.get("1") + map.get("0");
		result.put("num", sum * 1.0);
		double info = (map.get("1") * 1.0 / sum)
				* (Math.log((map.get("1") * 1.0 / sum)) / Math.log(2))
				+ (map.get("0") * 1.0 / sum)
				* (Math.log((map.get("0") * 1.0 / sum)) / Math.log(2));

		result.put("info", -info);
		return result;
	}

	public static double  caculatetotalinfo(
			HashMap<String, HashMap<String, Integer>> map,  Map<String, Double> totalmap)
			throws IOException {

		
		double    result=0;
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			HashMap<String, Integer> hash = map.get(iter.next());

			int m0 = hash.get("0") == null ? 0 : hash.get("0");
			int m1 = hash.get("1") == null ? 0 : hash.get("1");
			
			int sum_key=m0+m1;
			
			
			if(m0!=0 & m1!=0){
			result+=sum_key/totalmap.get("num") *((m1 * 1.0 / sum_key)
					* (Math.log((m1 * 1.0 / sum_key)) / Math.log(2))
					+ (m0* 1.0 / sum_key)
					* (Math.log((m0 * 1.0 / sum_key)) / Math.log(2)));
			}else if(m0!=0 & m1==0){
				
				result+=sum_key/totalmap.get("num") *(0+
						+ (m0* 1.0 / sum_key)
						* (Math.log((m0 * 1.0 / sum_key)) / Math.log(2)));
			}else if(m0==0 & m1!=0){
				result+=sum_key/totalmap.get("num") *((m1 * 1.0 / sum_key)
						* (Math.log((m1 * 1.0 / sum_key)) / Math.log(2)+ 0));
				
				
			}

		}
//		System.out.println(result);
		
		return   totalmap.get("info")+result;
		
		

	}
	public static void main(String[] args) throws IOException {

		System.err.println(Math.log(8) / Math.log(2));
		// System.out.println(caculateinfo("d:\\wilson.zhou\\Desktop\\part-r-00005"));
		
		ArrayList<List<String>> list = DataHandle.readlog("d:\\wilson.zhou\\Desktop\\part-r-00005");
		 Map<String, Double> totalmap=caculateinfo(list);
		 HashMap<String, HashMap<String, Integer>> map=DataHandle.getkeyValue(list, 1);
		 System.out.println(caculatetotalinfo(map,totalmap));
		  	 
	}

}
