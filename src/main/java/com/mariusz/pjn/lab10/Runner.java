package com.mariusz.pjn.lab10;

public class Runner {
    private static final DictionaryCLP dictionaryCLP = new DictionaryCLP("clp/lib");

    public static void main(String[] args) {
        String s = dictionaryCLP.clp_bform(dictionaryCLP.clp_rec("piłki").get(0));
        System.out.println(s);
    }
}
