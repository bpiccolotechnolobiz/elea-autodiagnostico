package com.ar.tbz.main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestLista {

	public static void main(String[] args) {
		String[] instrucc = { "FRBLLLL", "LLRFB", "FFFF" };

		for (String item : instrucc) {
			if (true) {

			}
		}

		List<String> list = Arrays.asList(instrucc);
		List<String> filtro = list.stream().filter(i -> i.contains("B"))
				.map(mapEntry -> String.valueOf(mapEntry.charAt(0))).collect(Collectors.toList());
		System.out.println(filtro);
	}

}
