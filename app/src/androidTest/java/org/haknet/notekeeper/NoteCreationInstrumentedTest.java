package org.haknet.notekeeper;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static androidx.test.espresso.Espresso.pressBack;

@RunWith(AndroidJUnit4.class)
public class NoteCreationInstrumentedTest {
    static DataManager dataManager;

    @BeforeClass
    public static void classSetup() {
        dataManager = DataManager.getInstance();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void createNewNote() {
        final CourseInfo courseInfo = dataManager.getCourse("java_lang");
        final String title = "Test note title";
        final String textBody = "This is the body of the test note";
        onView(withId(R.id.addNote)).perform(click());
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class), equalTo(courseInfo))).perform(click());
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(courseInfo.getTitle()))));

        onView(withId(R.id.text_note_title)).perform(typeText(title));
        onView(withId(R.id.text_note_text)).perform(typeText(textBody), closeSoftKeyboard());
        onView(withId(R.id.text_note_title)).check(matches(withText(containsString(title))));
        pressBack();

        int noteIndex = dataManager.getNotes().size() -1;
        NoteInfo noteInfo = dataManager.getNotes().get(noteIndex);

        assertEquals(courseInfo, noteInfo.getCourse());
        assertEquals(title, noteInfo.getTitle());
        assertEquals(textBody, noteInfo.getText());
    }
}