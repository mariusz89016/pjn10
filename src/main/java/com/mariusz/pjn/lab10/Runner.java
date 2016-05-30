package com.mariusz.pjn.lab10;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {
    private static final DictionaryCLP dictionaryCLP = new DictionaryCLP("clp/lib");

    public static void main(String[] args) throws IOException {
        List<Note> notes = new NotesProvider("pap.txt").getNotes();
        Map<String, Map<Case, Integer>> przyimekToMap = new HashMap<>();

        for (Note note : notes.subList(0, 5000)) {
            List<String> words = note.getWords();
//            System.out.println(words);
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                List<Integer> integers = dictionaryCLP.clp_rec(word);
                if(integers.size()>0) {
                    DictionaryCLP.WordType wordType = null;
                    try {

                        wordType = dictionaryCLP.clp_pos(integers.get(0));
                    }
                    catch (IndexOutOfBoundsException e) {
                        System.out.println(word);
                        System.out.println(wordType);
                        System.exit(123);
                    }
                    if(wordType == DictionaryCLP.WordType.PRZYIMEK) {
                        przyimekToMap.putIfAbsent(word, new HashMap<>());
                        int j = i+1;
                        while(j<words.size()) {
                            String nextWord = words.get(j);
                            List<Integer> integers1 = dictionaryCLP.clp_rec(nextWord);
                            if(integers1.size()>0) {
                                DictionaryCLP.WordType wordType1 = dictionaryCLP.clp_pos(integers1.get(0));
                                if(wordType1== DictionaryCLP.WordType.RZECZOWNIK) {
                                    List<String> list = dictionaryCLP.clp_formv(integers1.get(0));
                                    List<Case> cases = new LinkedList<>();
                                    for (int k = 0; k < list.size(); k++) {
                                        if(list.get(k).equals(nextWord)) {
                                            cases.add(getCase(k));
                                        }
                                    }

                                    Map<Case, Integer> caseIntegerMap1 = przyimekToMap.get(word);
                                    for (Case aCase : cases) {
                                        int value = caseIntegerMap1.getOrDefault(aCase, 0);
                                        caseIntegerMap1.put(aCase, value+1);
                                    }
//                                    System.out.println(cases);

//                                    System.out.println(word + " + " + nextWord);
//                                    System.out.println(list);
                                    i = j+1;
                                    break;
                                }
                            }
                            j++;
                        }
                    }
                }

            }
        }
        for (String s : przyimekToMap.keySet()) {
            Map<Case, Integer> caseDoubleMap = przyimekToMap.get(s);
            Histogram<Case> histogram = new Histogram<>();
            for (Map.Entry<Case, Integer> thing : caseDoubleMap.entrySet()) {
                for (int i = 0; i < thing.getValue(); i++) {
                    histogram.addDataPoint(thing.getKey());
                }
            }
            Map<Case, Double> histo = histogram.histo();
            List<Map.Entry<Case, Double>> lista = histo.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).collect(Collectors.toList());
            System.out.println(s);
            System.out.println(lista);
            System.out.println("===============");
        }
//        System.out.println(przyimekToMap);
    }
    static Case getCase(int pos) {
        switch (pos%7) {
            case 0:
                return Case.MIANOWNIK;
            case 1:
                return Case.DOPŁENIACZ;
            case 2:
                return Case.CELOWNIK;
            case 3:
                return Case.BIERNIK;
            case 4:
                return Case.NARZĘDNIK;
            case 5:
                return Case.MIEJSCOWNIK;
            case 6:
                return Case.WOŁACZ;

        }
        return null;
    }
}
