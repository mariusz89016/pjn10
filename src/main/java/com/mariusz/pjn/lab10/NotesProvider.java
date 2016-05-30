package com.mariusz.pjn.lab10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NotesProvider {
    private final String fileName;

    public NotesProvider(String fileName) {
        this.fileName = fileName;
    }

    public List<Note> getNotes() throws IOException {
        Set<Note> notes = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            long lastId = 0;

            while (line != null) {
                if (line.startsWith("#")) {
                    if(stringBuilder.length()!=0) {
                        notes.add(new Note(stringBuilder.toString(), lastId));
                    }
                    stringBuilder = new StringBuilder();
                    lastId = Long.parseLong(line.substring(1));
                } else {
                    stringBuilder.append(line.trim());
                    stringBuilder.append(" ");
                }
                line = bufferedReader.readLine();
            }
            if(stringBuilder.length()!=0) {
                notes.add(new Note(stringBuilder.toString(), lastId));
            }
        }
        return new LinkedList<>(notes);
    }
}
