package net.ayman.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ayman.R;
import net.ayman.helpers.Config;

import java.util.ArrayList;
import java.util.List;

public class NumbersFragment extends Fragment {

    private ProgressBar progressBar;
    private VideoView videoView;
    private EditText editTextInput;


    private List<String> signVideosList;
    private List<String> videos = new ArrayList<>();

    private int videoIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_numbers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        signVideosList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference(Config.NODE_NUMBERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (dataSnapshot.exists()) {
                            signVideosList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                signVideosList.add(ds.getValue(String.class));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        editTextInput = view.findViewById(R.id.editTextInput);
        videoView = view.findViewById(R.id.videoView);

        view.findViewById(R.id.buttonTranslate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateToSignLang();
            }
        });


        view.findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.buttonStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
            }
        });

        view.findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoIndex++;
                if (videoIndex < videos.size()) {
                    videoView.setVideoPath(videos.get(videoIndex));
                    videoView.start();
                }
            }
        });
    }

    private void translateToSignLang() {
        String input = editTextInput.getText().toString().trim();

        if (input.isEmpty()) {
            editTextInput.setError("Enter a number");
            editTextInput.requestFocus();
            return;
        }


        videos.clear();

        for (int i = 0; i < input.length(); i++) {
            videos.add(signVideosList.get(Integer.parseInt(String.valueOf(input.charAt(i)))));
        }

        videoIndex = 0;

        videoView.setVideoPath(videos.get(0));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (videoIndex < videos.size()) {
                    videoIndex++;
                    videoView.setVideoPath(videos.get(videoIndex));
                    videoView.start();
                }
            }
        });


    }
}
