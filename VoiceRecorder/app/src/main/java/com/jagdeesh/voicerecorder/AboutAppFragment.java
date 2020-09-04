package com.jagdeesh.voicerecorder;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.microsoft.appcenter.crashes.Crashes;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAppFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private TextView privacyPolicy;
//    private TextView termsOfUse;
    private TextView backButton;
    private ImageButton imageButton;

    private ImageButton shareImageBtn;
    private TextView shareText;

    public AboutAppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);
        backButton = view.findViewById(R.id.back_button_text);
        imageButton = view.findViewById(R.id.backImgBtn);
        shareImageBtn = view.findViewById(R.id.shareButton);
        shareText = view.findViewById(R.id.shareText);


        // Privacy policy link
        privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        String privacyPolicyText = "<a href='https://www.google.com/'>Privacy Policy</a>";
        privacyPolicy.setText(Html.fromHtml(privacyPolicyText));

        backButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        shareImageBtn.setOnClickListener(this);
        shareText.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button_text:
                navController.navigate(R.id.action_aboutAppFragment_to_audioRecordFragment);
                break;
            case R.id.backImgBtn:
                navController.navigate(R.id.action_aboutAppFragment_to_audioRecordFragment);
                break;
            case R.id.shareButton:
                shareApp();
                break;
            case R.id.shareText:
                shareApp();
                break;
        }
    }

    // App Share code

    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Voice Recorder");
            String shareMessage= "\nI found really great app for recording audio files. I've been using it for recording lecture notes, singing etc.,\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.jagdeesh.voicerecorder\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch(Exception e) {
            Crashes.trackError(e);
        }
    }
}