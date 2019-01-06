package net.ayman.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.ayman.R;
import net.ayman.database.DatabaseHelper;
import net.ayman.helpers.Alphabet;
import net.ayman.helpers.HelperMethods;

import java.util.ArrayList;
import java.util.List;

public class AlphabetsFragment extends Fragment {

    private EditText editTextInput;
    private ProgressBar progressBar;
    private int imageIndex = 0;
    private List<Alphabet> alphabetList;
    private DatabaseHelper db;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alphabets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHelper(getActivity());
        alphabetList = new ArrayList<>();

        listView = view.findViewById(R.id.listView);
        editTextInput = view.findViewById(R.id.editTextInput);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        view.findViewById(R.id.buttonTranslate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateToSignLang();
            }
        });
    }

    private void translateToSignLang() {
        final String input = editTextInput.getText().toString().trim();

        if (input.isEmpty()) {
            editTextInput.setError("Enter something");
            editTextInput.requestFocus();
            return;
        }

        alphabetList.clear();
        for (int i = 0; i < input.length(); i++) {
            String alpha = String.valueOf(input.charAt(i));
            String uriString = db.getVideo(alpha, "Alphabet");
            if (uriString != null) {
                alphabetList.add(new Alphabet(alpha, HelperMethods.getBitmap(uriString)));
            }
        }

        imageIndex = 0;

        if (alphabetList.size() <= 0) {
            Toast.makeText(getActivity(), "Nothing found on the database", Toast.LENGTH_LONG).show();
            return;
        }

        listView.setAdapter(new ArrayAdapter<Alphabet>(getActivity(), R.layout.my_list, alphabetList) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.my_list, parent, false);

                TextView textView = view.findViewById(R.id.textView);
                ImageView imageView = view.findViewById(R.id.imageView);
                Alphabet alpha = alphabetList.get(position);
                textView.setText(alpha.alpha);
                imageView.setImageBitmap(alpha.image);

                return view;
            }
        });
    }

}
