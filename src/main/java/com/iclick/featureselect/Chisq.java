package com.iclick.featureselect;

import java.util.ArrayList;
import java.util.List;

public class Chisq {

	public static void main(String[] args) {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1.add(19);
		list1.add(34);
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		list2.add(24);
		list2.add(10);

		double result = chisq(list1, list2);
		System.out.println(result);
	}

	public static double chisq(List<Integer> target1, List<Integer> target2) {

		if (target1.size() == 0 || target2.size() == 0
				|| (target1.size() != target2.size())) {
			System.out.println("不合法的给定的list");
			return -99.9;
		} else {
			int target1_sum = getListSum(target1);
			int target2_sum = getListSum(target2);
			int total = target1_sum + target2_sum;

			int size = target1.size();
			double result = 0.0;

			for (int i = 0; i < size; i++) {
				
				result += Math.pow((target1.get(i) - target1_sum * 1.0
						* (target1.get(i) + target2.get(i)) / total), 2)
						/ (target1_sum * 1.0
								* (target1.get(i) + target2.get(i)) / total);
				
				
				
				
				result += Math.pow((target2.get(i) - target2_sum * 1.0
						* (target1.get(i) + target2.get(i)) / total), 2)
						/ (target2_sum * 1.0
								* (target1.get(i) + target2.get(i)) / total);
				
				System.out.println(result);

			}
			return result;

		}
	}

	public static int getListSum(List<Integer> list) {
		int sum = 0;
		for (int elment : list) {
			sum += elment;
		}
		return sum;

	}

}
