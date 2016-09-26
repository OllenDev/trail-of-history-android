package org.charmeck.trailofhistory.core.ui.poi.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import java.util.List;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import org.charmeck.trailofhistory.core.R;
import org.charmeck.trailofhistory.core.R2;
import org.charmeck.trailofhistory.core.model.PointOfInterest;

/**
 * Created by Trey Robinson on 3/8/16.
 */
public class PointOfInterestAdapter extends RecyclerView.Adapter<PointOfInterestAdapter.ViewHolder>
    implements POIClickHandler {

  private List<PointOfInterest> pointOfInterestList;
  private POIClickHandler clickHandler;

  static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R2.id.poiName) TextView name;
    @BindView(R2.id.poiDescription) TextView description;
    @BindView(R2.id.poiImage) ImageView image;

    private PointOfInterest pointOfInterest;
    private POIClickHandler clickHandler;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public ViewHolder(View itemView, POIClickHandler clickHandler) {
      this(itemView);
      this.clickHandler = clickHandler;
      itemView.setOnClickListener(this);
    }

    public void bindPOI(PointOfInterest pointOfInterest) {
      this.pointOfInterest = pointOfInterest;
      name.setText(pointOfInterest.getName());

      Context context = image.getContext();
      Picasso.with(context)
          .load(pointOfInterest.getImageUrl())
          .transform(new BlurTransformation(context, 2, 2))
          .into(image);
    }

    @Override public void onClick(View v) {
      if (clickHandler != null) {
        clickHandler.handlePOIClicked(pointOfInterest);
      }
    }
  }

  public PointOfInterestAdapter(List<PointOfInterest> pointOfInterestList) {
    this.pointOfInterestList = pointOfInterestList;
  }

  public PointOfInterestAdapter(List<PointOfInterest> pointOfInterestList,
      POIClickHandler clickHandler) {
    this.pointOfInterestList = pointOfInterestList;
    this.clickHandler = clickHandler;
  }

  public void addPointOfInterest(PointOfInterest pointOfInterest) {
    pointOfInterestList.add(pointOfInterest);
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.row_point_of_interest, parent, false);
    return new ViewHolder(v, clickHandler);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    PointOfInterest poi = pointOfInterestList.get(position);
    holder.bindPOI(poi);
  }

  @Override public int getItemCount() {
    return pointOfInterestList.size();
  }

  @Override public void handlePOIClicked(PointOfInterest pointOfInterest) {
    if (clickHandler != null) {
      clickHandler.handlePOIClicked(pointOfInterest);
    }
  }
}