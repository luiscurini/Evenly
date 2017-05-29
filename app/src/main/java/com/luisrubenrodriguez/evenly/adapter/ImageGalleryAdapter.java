package com.luisrubenrodriguez.evenly.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.luisrubenrodriguez.evenly.R;

import java.util.List;

/**
 * Created by GamingMonster on 25.05.2017.
 * Pager Adapter that will show the venue's photo. It uses Glide for image caching.
 */

public class ImageGalleryAdapter extends PagerAdapter {

    private static final String TAG = "ImageGalleryAdapter";
    private final List<String> mImagesUrl;
    private final LayoutInflater mLayoutInflater;
    private static final String NOIMAGEURL = "http://www.bernunlimited.com/c.4436185/sca-dev-vinson/img/no_image_available.jpeg";

    public ImageGalleryAdapter(Context context, List<String> imagesUrl) {
        super();
        mImagesUrl = imagesUrl;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * If no images available the count will return 1 to show a default image.
     *
     * @return amount of images inside the list. If empty it will return 1.
     */
    @Override
    public int getCount() {
        return (null == mImagesUrl || mImagesUrl.size() == 0) ? 1 : mImagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Inflates the layout and add the images to the container.
     *
     * @param container ViewGroup that will contain the layout
     * @param position  currently being shown.
     * @return View Object.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.gallery_item);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.gallery_load);

        if ((mImagesUrl == null || mImagesUrl.size() == 0) && position == 0) {
            displayGalleryItem(imageView, progressBar, NOIMAGEURL);
        } else {
            displayGalleryItem(imageView, progressBar, mImagesUrl.get(position));
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView((View) object);

            // Try to clear resources used for displaying this view
            Glide.clear(((View) object).findViewById(R.id.gallery_item));
            unbindDrawables((View) object);
            object = null;
        } catch (Exception e) {
            Log.d(TAG, "destroyItem: exception");
        }
    }

    /**
     * Recursively unbind any resources from the provided view. This method will clear the resources of all the
     * children of the view before invalidating the provided view itself.
     *
     * @param view The view for which to unbind resource.
     */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    /**
     * Displays a photo inside the imageView
     *
     * @param galleryView ImageView where the photo should be shown
     * @param progressBar ProgressBar that will be shown while the image is retrieved.
     * @param url         Url of the photo
     */
    private void displayGalleryItem(ImageView galleryView, final ProgressBar progressBar, String url) {
        if (null != url) {

            progressBar.setVisibility(View.VISIBLE);
            Glide.with(galleryView.getContext())
                    .load(url)
                    .fitCenter() // scale type
                    //This will hide the progress bar.
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(galleryView);
        }
    }
}
