package org.charmeck.trailofhistory.ranger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import org.charmeck.trailofhistory.core.model.PointOfInterest;
import org.charmeck.trailofhistory.ranger.manager.PointOfInterestManager;

public class NewPointOfInterestActivity extends AppCompatActivity {

  private static final String KEY_POI = "pointOfInterest";
  private static final int REQUEST_CODE_LOCATION = 1;

  @BindView(R.id.poiname) EditText nameField;
  @BindView(R.id.poilocation) EditText locationField;
  @BindView(R.id.description) EditText descriptionField;

  private PointOfInterest pointOfInterest;

  public static Intent newInstance(Context context) {
    return new Intent(context, NewPointOfInterestActivity.class);
  }

  /**
   * Used for editing POI
   */
  public static Intent newInstance(Context context, PointOfInterest pointOfInterest) {
    Intent intent = new Intent(context, NewPointOfInterestActivity.class);
    intent.putExtra(KEY_POI, pointOfInterest);

    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_point_of_interest);
    ButterKnife.bind(this);

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    PointOfInterest poi = getIntent().getParcelableExtra(KEY_POI);
    if (poi != null) {
      this.pointOfInterest = poi;
      updateViews();
    }
  }

  @OnClick({ R.id.saveButton, R.id.locationButton }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.saveButton:
        validateAndSave();
        break;
      case R.id.locationButton:
        startActivityForResult(MapActivity.newInstance(this, pointOfInterest),
            REQUEST_CODE_LOCATION);
      default:
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_LOCATION && resultCode == RESULT_OK) {
      if (pointOfInterest == null) {
        pointOfInterest = new PointOfInterest();
      }
      pointOfInterest.setLatitude(data.getDoubleExtra(MapActivity.EXTRA_LATITUDE, 0));
      pointOfInterest.setLongitude(data.getDoubleExtra(MapActivity.EXTRA_LONGITUDE, 0));
      updateViews();
    }
  }

  private void validateAndSave() {
    if (validateForm()) {
      save();
    }
  }

  private void save() {
    if (pointOfInterest == null) {
      pointOfInterest = new PointOfInterest();
    }

    pointOfInterest.setName(nameField.getText().toString());
    pointOfInterest.setDescription(descriptionField.getText().toString());
    pointOfInterest = PointOfInterestManager.getInstance()
        .savePOI(pointOfInterest, new DatabaseReference.CompletionListener() {

          @Override
          public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            if (databaseError == null) {
              //go to detail view
              startActivity(
                  DetailPointOfInterestActivity.newInstance(NewPointOfInterestActivity.this,
                      pointOfInterest));
              finish();
            }
          }
        });
  }

  private boolean validateForm() {
    boolean valid = true;

    if (TextUtils.isEmpty(nameField.getText())) {
      nameField.setError(getString(R.string.error_required));
      valid = false;
    }

    return valid;
  }

  private void updateViews() {
    nameField.setText(pointOfInterest.getName());
    locationField.setText(
        String.format("%s, %s", pointOfInterest.getLatitude(), pointOfInterest.getLongitude()));
    descriptionField.setText(pointOfInterest.getDescription());
  }
}
