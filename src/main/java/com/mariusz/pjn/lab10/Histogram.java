package com.mariusz.pjn.lab10;

import java.util.HashMap;
import java.util.Map;

public class Histogram<T> {
    private final Map<T, Long> frequencyMap = new HashMap<>();

    public void addDataPoint(T bigram) {
        frequencyMap.put(bigram, frequencyMap.getOrDefault(bigram, 0l) + 1);
    }

    @Override
    public String toString() {
        return "Histogram{" +
                "frequencyMap=" + frequencyMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Histogram histogram = (Histogram) o;

        return frequencyMap != null ? frequencyMap.equals(histogram.frequencyMap) : histogram.frequencyMap == null;

    }

    @Override
    public int hashCode() {
        return frequencyMap != null ? frequencyMap.hashCode() : 0;
    }

    public Map<T, Double> histo() {
        final long sum = frequencyMap.values().stream().mapToLong(Long::longValue).sum();
        final Map<T, Double> histogram = new HashMap<>();
        frequencyMap.entrySet().forEach(e -> histogram.put(e.getKey(), ((double)e.getValue())/sum));
        return histogram;
    }

}
