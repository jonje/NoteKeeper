package org.haknet.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import org.haknet.notekeeper.databinding.FragmentNoteListBinding;

import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentNoteListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNoteListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDisplayContent();
//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    private void initializeDisplayContent() {
        final ListView listNotes = this.binding.listNotes;
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        ArrayAdapter<NoteInfo> arrayAdapter =
                new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, notes);
        binding.listNotes.setAdapter(arrayAdapter);

        binding.listNotes.setOnItemClickListener((adapterView, view, position, l) -> {
            NoteInfo noteInfo = (NoteInfo) listNotes.getItemAtPosition(position);
            SecondFragmentDirections.ActionNoteListToNote action = SecondFragmentDirections.actionNoteListToNote(noteInfo);
            Navigation.findNavController(view).navigate(action);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}