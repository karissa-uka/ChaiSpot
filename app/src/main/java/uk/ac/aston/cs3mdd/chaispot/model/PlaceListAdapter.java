package uk.ac.aston.cs3mdd.chaispot.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.aston.cs3mdd.chaispot.R;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder> {

    private List<MyPlace> mPlaceList;
    private final LayoutInflater mInflater;
    private OnPlaceItemClickListener onPlaceItemClickListener;
    private OnAddItemClickListener onAddItemClickListener;

    public interface OnPlaceItemClickListener {
        void onPlaceItemClick(MyPlace place);
    }

    public interface OnAddItemClickListener {
        void onAddItemClick(MyPlace place);
    }

    public PlaceListAdapter(Context context, List<MyPlace> placeList,
                            OnPlaceItemClickListener onPlaceItemClickListener,
                            OnAddItemClickListener onAddItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.mPlaceList = placeList;
        this.onPlaceItemClickListener = onPlaceItemClickListener;
        this.onAddItemClickListener = onAddItemClickListener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.place_item,
                parent, false);
        return new PlaceViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        MyPlace myPlace = mPlaceList.get(position);
        holder.place = myPlace;
        String name = myPlace.getName();
        holder.placeNameView.setText(name);
    }

    @Override
    public int getItemCount() {
        return this.mPlaceList.size();
    }

    public void updateData(List<MyPlace> list) {
        this.mPlaceList = list;
        notifyDataSetChanged();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        public final TextView placeNameView;
        public final ImageView placeIcon;
        final PlaceListAdapter mAdapter;
        public MyPlace place;
        public Button button;

        public PlaceViewHolder(@NonNull View itemView, PlaceListAdapter adapter) {
            super(itemView);
            placeNameView = itemView.findViewById(R.id.placename);
            this.placeIcon = itemView.findViewById(R.id.placeicon);
            this.mAdapter = adapter;
            this.button = itemView.findViewById(R.id.add_button);
            this.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onAddItemClickListener != null) {
                        onAddItemClickListener.onAddItemClick(mPlaceList.get(position));
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onPlaceItemClickListener != null) {
                        onPlaceItemClickListener.onPlaceItemClick(mPlaceList.get(position));
                    }
                }
            });
        }
    }
}
