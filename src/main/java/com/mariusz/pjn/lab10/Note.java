package com.mariusz.pjn.lab10;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Note {
    protected final String note;

    public long getId() {
        return id;
    }

    private final long id;

    protected Note(String note, long id) {
        this.note = note;
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public List<String> getWords() {
        return Arrays.asList(note.split(" ")).stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .map(str -> str.replace(",", ""))
                .map(str -> str.replace("\"", ""))
                .map(str -> str.replace("'", ""))
                .map(str -> str.replace(";", ""))
                .map(str -> str.replace(":", ""))
                .map(str -> str.replace("(", ""))
                .map(str -> str.replace(")", ""))
                .map(str -> str.replace("?", ""))
                .map(str -> str.replace("!", ""))
                .map(str -> str.replace("-", ""))
                .map(str -> str.replace("\u0084", ""))
                .map(str -> str.replace(".", ""))
                .filter(word -> !word.equals(""))
                .filter(word -> !word.equals("-"))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Note{" +
                "note='" + note + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note1 = (Note) o;

        return note != null ? note.equals(note1.note) : note1.note == null;

    }

    @Override
    public int hashCode() {
        return note != null ? note.hashCode() : 0;
    }
}