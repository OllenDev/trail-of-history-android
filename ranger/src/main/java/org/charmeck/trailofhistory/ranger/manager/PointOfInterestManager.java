package org.charmeck.trailofhistory.ranger.manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import org.charmeck.trailofhistory.core.model.PointOfInterest;

/**
 * Created by Trey Robinson on 7/9/16.
 */
public class PointOfInterestManager {

  private static PointOfInterestManager instance;

  private static final String KEY_POI = "pointOfInterest";

  private DatabaseReference database;

  public static PointOfInterestManager getInstance() {

    if (instance == null) {
      instance = new PointOfInterestManager();
    }

    return instance;
  }

  private PointOfInterestManager() {

    database = FirebaseDatabase.getInstance().getReference();
  }

  public PointOfInterest savePOI(PointOfInterest pointOfInterest,
      DatabaseReference.CompletionListener completionListener) {

    if (pointOfInterest.getUid() == null) {
      pointOfInterest.setUid(database.child(KEY_POI).push().getKey());
    }

    database.child(KEY_POI)
        .child(pointOfInterest.getUid())
        .setValue(pointOfInterest, completionListener);

    return pointOfInterest;
  }

  public void getPointsofInterest(final PointOfInterestListCallback callback) {
    database.child(KEY_POI).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        List<PointOfInterest> poiList = new ArrayList<PointOfInterest>();
        for (DataSnapshot poiSnapshot : dataSnapshot.getChildren()) {
          PointOfInterest poi = poiSnapshot.getValue(PointOfInterest.class);
          poiList.add(poi);
        }
        callback.pointsOfInterestRetrieved(poiList);
      }

      @Override public void onCancelled(DatabaseError databaseError) {
      }
    });
  }

  public interface PointOfInterestListCallback {
    void pointsOfInterestRetrieved(List<PointOfInterest> pointOfInterestList);
  }
}
