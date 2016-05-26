package in.vaksys.uploadimage;

import android.lib.recaptcha.ReCaptcha;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ReCaptcha.OnShowChallengeListener, ReCaptcha.OnVerifyAnswerListener {
    private static final String PUBLIC_KEY  = "6LeX8iATAAAAAB7ZB5Y6-Ht9ZGpqE-SJMb6Bb0xr";
    private static final String PRIVATE_KEY = "6LeX8iATAAAAAExwiAlsjVYjHeebdH-ksqOUChZQ";

    private ReCaptcha   reCaptcha;
    private ProgressBar progress;
    private EditText answer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        reCaptcha = (ReCaptcha)this.findViewById(R.id.recaptcha);
        progress  = (ProgressBar)this.findViewById(R.id.progress);
        answer    = (EditText)this.findViewById(R.id.answer);

        findViewById(R.id.verify).setOnClickListener(this);
        findViewById(R.id.reload).setOnClickListener(this);

        showChallenge();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.verify:
                this.verifyAnswer();

                break;

            case R.id.reload:
                this.showChallenge();

                break;
        }
    }

    @Override
    public void onChallengeShown(final boolean shown) {
        this.progress.setVisibility(View.GONE);

        if (shown) {
            // If a CAPTCHA is shown successfully, displays it for the user to enter the words
            this.reCaptcha.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.show_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAnswerVerified(final boolean success) {
        if (success) {
            Toast.makeText(this, R.string.verification_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.verification_failed, Toast.LENGTH_SHORT).show();
        }

        // (Optional) Shows the next CAPTCHA
        this.showChallenge();
    }

    private void showChallenge() {
        // Displays a progress bar while downloading CAPTCHA
        this.progress.setVisibility(View.VISIBLE);
        this.reCaptcha.setVisibility(View.GONE);

        this.reCaptcha.showChallengeAsync(MainActivity.PUBLIC_KEY, this);
    }

    private void verifyAnswer() {
        if (TextUtils.isEmpty(this.answer.getText())) {
            Toast.makeText(this, R.string.instruction, Toast.LENGTH_SHORT).show();
        } else {
            // Displays a progress bar while submitting the answer for verification
            this.progress.setVisibility(View.VISIBLE);
            this.reCaptcha.verifyAnswerAsync(MainActivity.PRIVATE_KEY, this.answer.getText().toString(), this);
        }
    }
}