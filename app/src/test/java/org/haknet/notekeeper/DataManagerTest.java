package org.haknet.notekeeper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataManagerTest {
    static DataManager dataManager;

    @BeforeAll
    public static void classSetUp() {
        dataManager = DataManager.getInstance();
    }

    @BeforeEach
    public void setup() {
        dataManager.getNotes().clear();
        dataManager.initializeExampleNotes();
    }

    @Test
    void createNewNote() {
        final CourseInfo courseInfo = dataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body text of the note.";

        int noteIndex = dataManager.createNewNote();
        NoteInfo noteInfo = dataManager.getNotes().get(noteIndex);
        noteInfo.setCourse(courseInfo);
        noteInfo.setTitle(noteTitle);
        noteInfo.setText(noteText);

        NoteInfo compareNote = dataManager.getNotes().get(noteIndex);
        assertEquals(courseInfo, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());
    }

    @Test
    void createNewNoteOneStepCreation() {
        final CourseInfo courseInfo = dataManager.getCourse("android_async");
        final String noteTitle = "Test not title";
        final String noteText = "Test note body";

        int noteIndex = dataManager.createNewNote(courseInfo, noteTitle, noteText);

        NoteInfo noteInfo = dataManager.getNotes().get(noteIndex);
        assertEquals(courseInfo, noteInfo.getCourse());
        assertEquals(noteTitle, noteInfo.getTitle());
        assertEquals(noteText, noteInfo.getText());
    }
}