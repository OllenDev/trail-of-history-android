package org.charmeck.trailofhistory.ranger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import org.charmeck.trailofhistory.core.model.PointOfInterest;

public class DetailPointOfInterestActivity extends AppCompatActivity {

  private static final String KEY_POI = "pointOfInterest";

  private PointOfInterest pointOfInterest;

  @BindView(R.id.nameField) TextView nameField;
  @BindView(R.id.locationField) TextView locationField;
  @BindView(R.id.descriptionField) TextView descriptionField;

  public static Intent newInstance(Context context, PointOfInterest pointOfInterest) {
    Intent intent = new Intent(context, DetailPointOfInterestActivity.class);
    intent.putExtra(KEY_POI, pointOfInterest);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Control Up/Back flow to main List
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_point_of_interest);
    ButterKnife.bind(this);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    PointOfInterest poi = getIntent().getParcelableExtra(KEY_POI);
    if (poi != null) {
      setPointOfInterest(poi);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.detail_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_edit:
        edit();

        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void edit() {
    if (pointOfInterest != null) {

      startActivity(NewPointOfInterestActivity.newInstance(this, pointOfInterest));
    }
  }

  private void setPointOfInterest(PointOfInterest pointOfInterest) {
    this.pointOfInterest = pointOfInterest;
    nameField.setText(pointOfInterest.getName());
    locationField.setText(
        getString(R.string.location_format_string, Double.toString(pointOfInterest.getLatitude()),
            Double.toString(pointOfInterest.getLongitude())));
    descriptionField.setText(pointOfInterest.getDescription());
  }
}
