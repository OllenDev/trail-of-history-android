package org.charmeck.trailofhistory;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.firebase.ui.auth.AuthUI;

public class ProfileActivity extends AuthenticatedActivity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    ButterKnife.bind(this);
  }

  @OnClick(R.id.signout_button)
  public void signOut() {
    AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> finish());
  }

}
