package net.ayman.fragments;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import net.ayman.R;
import net.ayman.database.DatabaseHelper;
import net.ayman.helpers.HelperMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class TranslateFragment extends Fragment {

    private EditText editTextInput;
    private VideoView videoView;
    private DatabaseHelper db;

    private int videoIndex = 0;
    private List<String> videos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videos = new ArrayList<>();

        db = new DatabaseHelper(getActivity());

        editTextInput = view.findViewById(R.id.editTextInput);
        videoView = view.findViewById(R.id.videoView);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());


        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    editTextInput.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        view.findViewById(R.id.buttonSpeak).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editTextInput.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editTextInput.setText("");
                        editTextInput.setHint("Listening...");
                        break;
                }
                return false;
            }
        });

        view.findViewById(R.id.buttonTranslate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateToSignLang();
            }
        });
    }


    private void translateToSignLang(){
        String input = editTextInput.getText().toString().trim();

        if (input.isEmpty()) {
            editTextInput.setError("Enter a number");
            editTextInput.requestFocus();
            return;
        }


        videos.clear();

        StringTokenizer st = new StringTokenizer(input, " ");

        while(st.hasMoreTokens()){
            String word = st.nextToken();
            String uriString = db.getVideo(word, "Phrase");
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
