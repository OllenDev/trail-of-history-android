package org.charmeck.trailofhistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import timber.log.Timber;

/**
 * Class to manage requiring a user to login
 *
 * Created by Chris Ollenburg on 9/26/16.
 */
public class AuthenticatedActivity extends AppCompatActivity {
  private static final String TAG = AuthenticatedActivity.class.getSimpleName();
  protected static final int RC_SIGN_IN = 1;

  private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
    @Override public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
      Timber.tag(TAG).d("Authstate changed: %s", firebaseAuth.getCurrentUser() != null ? "Signed In" : "Signed Out");
    }
  };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.addAuthStateListener(authStateListener);
    if (auth.getCurrentUser() == null) {
      startActivityForResult(
          AuthUI.getInstance()
              .createSignInIntentBuilder()
              .setLogo(R.mipmap.ic_launcher)
              .setProviders(AuthUI.EMAIL_PROVIDER, AuthUI.GOOGLE_PROVIDER)
              .setIsSmartLockEnabled(!BuildConfig.DEBUG)
              .build(),
          RC_SIGN_IN);
    } else {
      handleUserSignedIn();
    }
  }

  @Override protected void onDestroy() {
    FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      if (resultCode != RESULT_OK) {
        handleUserFailedToSignIn();
      } else {
        handleUserSignedIn();
      }
    }
  }

  protected void handleUserSignedIn() {

  }

  protected void handleUserFailedToSignIn() {

  }
}
