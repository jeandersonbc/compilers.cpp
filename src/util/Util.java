package util;


import java.util.ArrayList;
import java.util.List;

import core.Expression;
import core.Type;

import compiler.generated.*;

public class Util {
	
	public static <T> ArrayList<T> newList(T... elements) {
		
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0 ; i < elements.length ; i++) {
			list.add(elements[i]);
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] addArray(T[] array, T object) {
		
		List<T> list = new ArrayList<T>();
		for (int i = 0 ; i < array.length ; i++)
			list.add(array[i]);
		list.add(object);
		
		return (T[]) list.toArray();
	}
	
	public static Type[] convertToTypeArray(List<Expression> expressions) {
		Type[] types = new Type[expressions.size()];
		for (int i = 0 ; i < types.length ; i++)
			types[i] = expressions.get(i).getType();
		return types;
	}

}
