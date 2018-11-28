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
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.ayman.R;
import net.ayman.helpers.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphabetsFragment extends Fragment {

    private EditText editTextInput;
    private ProgressBar progressBar;
    private Map<String, String> alphabetsMap;
    private List<String> videos = new ArrayList<>();

    private int videoIndex = 0;

    private VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alphabets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoView = view.findViewById(R.id.videoView);
        editTextInput = view.findViewById(R.id.editTextInput);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        alphabetsMap = new HashMap<>();


        FirebaseDatabase.getInstance().getReference(Config.NODE_ALPHABETS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        alphabetsMap.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                alphabetsMap.put(ds.getKey(), ds.getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        view.findViewById(R.id.buttonTranslate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateToSignLang();
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
            editTextInput.setError("Enter something");
            editTextInput.requestFocus();
            return;
        }

        for (int i = 0; i < input.length(); i++) {
            String alphabet = String.valueOf(input.charAt(i));

            if (alphabetsMap.get(alphabet) != null)
                videos.add(alphabetsMap.get(alphabet.toUpperCase()));
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
