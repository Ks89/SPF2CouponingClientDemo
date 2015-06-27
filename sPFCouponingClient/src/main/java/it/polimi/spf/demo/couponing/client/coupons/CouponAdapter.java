package it.polimi.spf.demo.couponing.client.coupons;

/**
 * Created by Stefano Cappa on 22/06/15.
 */

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import it.polimi.spf.demo.couponing.client.Coupon;
import it.polimi.spf.demo.couponing.client.R;

/**
 * Class CouponAdapter with the new RecyclerView (Lollipop) and
 * {@link it.polimi.spf.demo.couponing.client.coupons.CouponAdapter.ViewHolder}
 * for performance reasons.
 * This class is the Adapter to represents data inside the {@link it.polimi.spf.demo.couponing.client.coupons.CouponManagerFragment}
 * <p></p>
 * Created by Stefano Cappa on 22/06/15.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private final ItemClickListener itemClickListener;

    /**
     * Constructor of the adapter.
     * @param itemClickListener ClickListener to obtain click actions over the recyclerview's elements.
     */
    public CouponAdapter(@NonNull ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);
    }

    /**
     * {@link it.polimi.spf.demo.couponing.client.coupons.CouponManagerFragment} implements this interface
     */
    public interface ItemClickListener {
        void itemClicked(final View view);
    }


    /**
     * The ViewHolder of this Adapter, useful to store e recycle element for performance reasons.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;
        private final ImageView photo;
        private final TextView title;
        private final TextView category;

        public ViewHolder(View view) {
            super(view);

            this.parent = view;

            photo = (ImageView) view.findViewById(R.id.coupon_entry_photo);
            title = (TextView) view.findViewById(R.id.coupon_entry_title);
            category = (TextView) view.findViewById(R.id.coupon_entry_category);
        }


        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View v = layoutInflater.inflate(R.layout.coupon_list_entry, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Coupon coupon = CouponList.getInstance().getCouponList().get(position);

        if (coupon != null) {
            viewHolder.photo.setImageBitmap(BitmapFactory.decodeByteArray(coupon.getPhoto(), 0, coupon.getPhoto().length));
            viewHolder.title.setText(coupon.getTitle());
            viewHolder.category.setText(coupon.getCategory());
        }

        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(v);
            }
        });
    }


    @Override
    public int getItemCount() {
        return CouponList.getInstance().getCouponList().size();
    }
}