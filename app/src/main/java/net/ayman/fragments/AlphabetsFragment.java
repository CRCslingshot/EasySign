package net.ayman.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
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
import net.ayman.database.DatabaseHelper;
import net.ayman.helpers.Config;
import net.ayman.helpers.HelperMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphabetsFragment extends Fragment {

    private EditText editTextInput;
    private ProgressBar progressBar;
    private int videoIndex = 0;
    private List<String> videos;
    private DatabaseHelper db;
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
                    byte[] base64Video = Base64.decode(videos.get(videoIndex), Base64.DEFAULT);
                    String path = HelperMethods.getVideo(base64Video);
                    if (path != null) {
                        videoView.setVideoPath(path);
                        videoView.start();
                    }
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
            String alpha = String.valueOf(input.charAt(i));
            String uriString = db.getVideo(alpha);
            if (uriString != null) {
                videos.add(uriString);
            }
        }

        videoIndex = 0;

        if (videos.size() <= 0) {
            Toast.makeText(getActivity(), "Nothing found on the database", Toast.LENGTH_LONG).show();
            return;
        }

        byte[] base64Video = Base64.decode(videos.get(0), Base64.DEFAULT);
        String path = HelperMethods.getVideo(base64Video);

        if (path != null) {
            videoView.setVideoPath(path);
            Log.d("NumbersFragment", videos.get(0));
            videoView.start();
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoIndex++;
                if (videoIndex < videos.size()) {
                    byte[] base64Video = Base64.decode(videos.get(videoIndex), Base64.DEFAULT);
                    String path = HelperMethods.getVideo(base64Video);
                    if (path != null) {
                        videoView.setVideoPath(path);
                        videoView.start();
                    }
                }
            }
        });
    }
}
