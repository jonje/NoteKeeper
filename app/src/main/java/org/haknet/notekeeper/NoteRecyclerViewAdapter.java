package org.haknet.notekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<NoteInfo> notes;

    public NoteRecyclerViewAdapter(Context context, List<NoteInfo> notes) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteInfo noteInfo = this.notes.get(position);
        holder.textCourse.setText(noteInfo.getCourse().getTitle());
        holder.textTitle.setText(noteInfo.getTitle());
        holder.currentPosition = position;

    }

    @Override
    public int getItemCount() {
        return this.notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTitle;
        public int currentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCourse = (TextView) itemView.findViewById(R.id.text_course);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(v -> {
                SecondFragmentDirections.ActionNoteListToNote action = SecondFragmentDirections.actionNoteListToNote(currentPosition);
                Navigation.findNavController(v).navigate(action);
            });
        }
    }
}
