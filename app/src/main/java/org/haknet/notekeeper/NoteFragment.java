package org.haknet.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.haknet.notekeeper.databinding.FragmentNoteBinding;

import java.util.List;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private static final int POSITION_NOT_SET = -1;
    private NoteInfo noteInfo;
    private int notePosition;
    private boolean isCancelling;
    private boolean isNewNote;
    private MenuHost menuHost;
    private MenuProvider menuProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuHost = requireActivity();
        menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.action_send_mail) {
                    sendEmail();
                    return true;
                } else if (id == R.id.action_cancel) {
                    isCancelling = true;
                    // Used to navigate up the stack to the previous fragment.
                    NavHostFragment.findNavController(NoteFragment.this).navigateUp();
                } else if (id == R.id.action_next) {
                    moveNext();
                }
                return false;
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                MenuItem menuItem = menu.findItem(R.id.action_next);
                int lastNoteIndex = DataManager.getInstance().getNotes().size() -1;
                menuItem.setEnabled(notePosition < lastNoteIndex);
                MenuProvider.super.onPrepareMenu(menu);
            }
        };

        menuHost.addMenuProvider(menuProvider);

        int notePosition = NoteFragmentArgs.fromBundle(getArguments()).getNotePosition();
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCourses.setAdapter(adapterCourses);
        isNewNote = POSITION_NOT_SET == notePosition;

        if (!isNewNote) {
            noteInfo = DataManager.getInstance().getNotes().get(notePosition);
            displayNote();

        } else {
            createNewNote();
        }

//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(NoteFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    private void moveNext() {
        saveNote();
        ++notePosition;
        noteInfo = DataManager.getInstance().getNotes().get(notePosition);
        displayNote();
        this.menuHost.invalidateMenu();
    }

    private void displayNote() {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(noteInfo.getCourse());
        binding.spinnerCourses.setSelection(courseIndex);

        binding.textNoteTitle.setText(noteInfo.getTitle());
        binding.textNoteText.setText(noteInfo.getText());
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote();
        noteInfo = dm.getNotes().get(notePosition);
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) binding.spinnerCourses.getSelectedItem();
        String subject = binding.textNoteTitle.getText().toString();
        String text = "Checkout what I learned in the Pluralsight course \"" +
                course.getTitle() + "\"\n" + binding.textNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);

    }

    private void saveNote() {
        this.noteInfo.setCourse((CourseInfo) this.binding.spinnerCourses.getSelectedItem());
        this.noteInfo.setTitle(this.binding.textNoteTitle.getText().toString());
        this.noteInfo.setText(this.binding.textNoteText.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isCancelling) {
            saveNote();
        } else {
            if (isNewNote) {
                DataManager.getInstance().removeNote(notePosition);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        if (this.menuHost != null) {
            this.menuHost.removeMenuProvider(this.menuProvider);
        }
    }

}