package ondotsystems.com.imagefinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ondotsystems.com.imagefinder.R;
import ondotsystems.com.imagefinder.model.PixabayImage;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<PixabayImage> imageArrayList;

    public ImageAdapter(Context context, ArrayList<PixabayImage> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        PixabayImage pixabayImage = imageArrayList.get(i);

        String imageUrl = pixabayImage.getImageUrl();
        int downloads = pixabayImage.getDownloads();

        Picasso.get()
                .load(imageUrl)
                .centerCrop()
                .fit()
                .into(imageViewHolder.searchImage);

        imageViewHolder.downloads.setText(Integer.toString(downloads));
    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView searchImage;
        TextView downloads;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            searchImage = itemView.findViewById(R.id.search_image);
            downloads = itemView.findViewById(R.id.tv_downloads);
        }
    }
}
