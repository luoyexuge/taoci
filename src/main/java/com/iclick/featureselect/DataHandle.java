package com.iclick.featureselect;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.util.HashMap;

public class DataHandle {

	public static ArrayList<List<String>> readlog(String path)
			throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(
				new FileInputStream(path)));
		ArrayList<List<String>> list = new ArrayList<List<String>>();
		String str;
		while ((str = buff.readLine()) != null) {
			list.add((List<String>) Arrays.asList(str.split(",")));
		}

		return list;

	}

	// 得到每种类型对应的种类

	public static HashMap<String, HashMap<String, Integer>> getkeyValue(
			ArrayList<List<String>> arrayList, int m) {
		HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();

		for (List<String> li : arrayList) {
			int size = li.size();

			String browstring = (String) li.get(m);
			String target;
			try {
				target = Integer.parseInt(li.get(10)) == 1 ? "1" : "0";
			} catch (Exception e) {

				target = "0";

			}

			if (map.containsKey(browstring)) {
				HashMap<String, Integer> has = map.get(browstring);

				if (has.containsKey(target)) {
					has.put(target, has.get(target) + 1);
				} else {
					has.put(target, 1);
				}
				map.put(browstring, has);

			} else {

				HashMap<String, Integer> has = new HashMap<String, Integer>();
				has.put(target, 1);
				map.put(browstring, has);
			}

		}

		return map;
	}

	// 得到算起卡方值所需的要list
	public static List<Integer> getlist(
			HashMap<String, HashMap<String, Integer>> map, String key) {
		List<Integer> list = new ArrayList<Integer>();

		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key1 = iter.next();
			// System.out.println("最后列表对应的值是："+key1);
			HashMap<String, Integer> temp = map.get(key1);

			if (temp.containsKey(key)) {
				list.add(temp.get(key));
			} else {
				list.add(0);
			}

		}
		return list;

	}

	public static void main(String[] args) throws IOException {

		HashMap<String, HashMap<String, Integer>> map = getkeyValue(
				(readlog("d:\\wilson.zhou\\Desktop\\part-r-00068")), 1);
		List<Integer> list1 = getlist(map, "1");
		List<Integer> list2 = getlist(map, "0");
		 System.out.println(list1);
		 System.out.println(list2);
		System.out.println(Chisq.chisq(list1, list2));

	}

}
