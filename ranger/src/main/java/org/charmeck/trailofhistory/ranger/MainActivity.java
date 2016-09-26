package org.charmeck.trailofhistory.ranger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import org.charmeck.trailofhistory.core.model.PointOfInterest;
import org.charmeck.trailofhistory.core.ui.poi.list.POIClickHandler;
import org.charmeck.trailofhistory.core.ui.poi.list.PointOfInterestAdapter;
import org.charmeck.trailofhistory.ranger.manager.PointOfInterestManager;

/**
 * Lists all points of interest currently on the trail of history
 */
public class MainActivity extends AuthenticatedActivity
    implements PointOfInterestManager.PointOfInterestListCallback, POIClickHandler {

  private static final String TAG = "MainActivity";

  @BindView(R.id.poiList) RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private PointOfInterestAdapter adapter;

  public static Intent newInstance(Context context) {
    return new Intent(context, MainActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.simple_divider);

    recyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
  }

  @Override protected void onResume() {
    super.onResume();

    showProgressDialog();
    PointOfInterestManager.getInstance().getPointsofInterest(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_sign_out:
        signOut();

        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @OnClick(R.id.fab) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fab:
        createPOI();
        break;
      default:
        break;
    }
  }

  private void signOut() {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.signOut();
  }

  private void createPOI() {
    startActivity(NewPointOfInterestActivity.newInstance(this));
  }

  @Override public void pointsOfInterestRetrieved(List<PointOfInterest> pointOfInterestList) {
    hideProgressDialog();
    if (pointOfInterestList != null) {
      adapter = new PointOfInterestAdapter(pointOfInterestList, this);
      recyclerView.setAdapter(adapter);
    }
  }

  @Override public void handlePOIClicked(PointOfInterest pointOfInterest) {
    startActivity(DetailPointOfInterestActivity.newInstance(this, pointOfInterest));
  }
}
