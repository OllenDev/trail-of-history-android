package org.charmeck.trailofhistory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity with a transparent interface that is simply used to route intents to activities
 * that need to be logged in. Doing it this way prevents the target activity which may
 * require authenticated operations from ever being loaded.
 *
 * Created by Chris Ollenburg on 9/26/16.
 */
public class AuthRoutingActivity extends AuthenticatedActivity {
  private static final String TAG = AuthRoutingActivity.class.getSimpleName();
  private static final String INTENT_KEY = "INTENT_KEY";

  private Intent destIntent;

  public static void attemptToOpen(Activity source, Intent destIntent) {
    Intent intent = new Intent(source, AuthRoutingActivity.class);
    intent.putExtra(INTENT_KEY, destIntent);
    source.startActivity(intent);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    destIntent = getIntent().getParcelableExtra(INTENT_KEY);
    super.onCreate(savedInstanceState);
  }

  // TODO move these methods into an interface that gets passed in?
  @Override protected void handleUserSignedIn() {
    startActivity(destIntent);
    finish();
  }

  @Override protected void handleUserFailedToSignIn() {
    super.handleUserFailedToSignIn();
    // TODO Implement
    finish();
  }
}
