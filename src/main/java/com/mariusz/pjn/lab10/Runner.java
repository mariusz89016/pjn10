package com.mariusz.pjn.lab10;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Runner {
    private static final DictionaryCLP dictionaryCLP = new DictionaryCLP("clp/lib");

    public static void main(String[] args) throws IOException {
        List<Note> notes = new NotesProvider("pap.txt").getNotes();
        Map<String, Map<Case, Long>> przyimekToMap = new HashMap<>();

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
                                    int amount = 0;
                                    for (int k = 0; k < list.size(); k++) {
                                        if(list.get(k).equals(nextWord)) {
                                            cases.add(getCase(k));
                                            amount++;
                                        }
                                    }

                                    Map<Case, Long> caseIntegerMap1 = przyimekToMap.get(word);
                                    for (Case aCase : cases) {
                                        Long value = caseIntegerMap1.getOrDefault(aCase, 0L);
                                        Double pow = Math.pow((7 - amount), 10);
                                        caseIntegerMap1.put(aCase, value+1+pow.longValue());
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
            Map<Case, Long> caseDoubleMap = przyimekToMap.get(s);
//            Histogram<Case> histogram = new Histogram<>();
//            for (Map.Entry<Case, Integer> thing : caseDoubleMap.entrySet()) {
//                for (int i = 0; i < thing.getValue(); i++) {
//                    histogram.addDataPoint(thing.getKey());
//                }
//            }
//            Map<Case, Double> histo = histogram.histo();
//            List<Map.Entry<Case, Double>> lista = histo.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).collect(Collectors.toList());
            long sum = przyimekToMap.get(s).values().stream().mapToLong(Long::longValue).sum();


            System.out.println(s);
            List<Map.Entry<Case, Double>> collect = przyimekToMap.get(s).entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue() / (double) sum))
                    .collect(Collectors.toList());
            double thereshold = 0d;
            List<Map.Entry<Case, Double>> output = new LinkedList<>();

            for (int i = 0; thereshold<0.6 && i<collect.size(); i++) {
                Case key = collect.get(i).getKey();
                double value = collect.get(i).getValue();
                output.add(new AbstractMap.SimpleEntry<>(key, value));
                thereshold+= value;
            }
            System.out.println(output);
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
