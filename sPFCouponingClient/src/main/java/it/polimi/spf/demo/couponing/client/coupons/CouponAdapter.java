package it.polimi.spf.demo.couponing.client.coupons;

/**
 * Created by Stefano Cappa on 22/06/15.
 */

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import it.polimi.spf.demo.couponing.client.Coupon;
import it.polimi.spf.demo.couponing.client.R;
import lombok.Getter;

/**
 * Class CouponAdapter with the new RecyclerView (Lollipop) and
 * {@link it.polimi.spf.demo.couponing.client.coupons.CouponAdapter.ViewHolder}
 * for performance reasons.
 * This class is the Adapter to represents data inside the {@link it.polimi.spf.demo.couponing.client.coupons.CouponManagerFragment}
 * <p></p>
 * Created by Stefano Cappa on 28/06/15.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private final MultiSelector mMultiSelector;
    private final OnClickInterface clickListener;

    /**
     * Constructor of the adapter.
     * @param mMultiSelector
     * @param clickListener
     */
    public CouponAdapter(MultiSelector mMultiSelector, OnClickInterface clickListener) {
        this.mMultiSelector = mMultiSelector;
        this.clickListener = clickListener;
    }

    /**
     * {@link it.polimi.spf.demo.couponing.client.coupons.CouponManagerFragment} implements this interface
     */
    public interface OnClickInterface {
        void longClickOnItem(CouponAdapter.ViewHolder viewHolder);
        void clickOnItem(CouponAdapter.ViewHolder viewHolder);
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
        viewHolder.bind(coupon);
    }

    @Override
    public int getItemCount() {
        return CouponList.getInstance().getCouponList().size();
    }

    /**
     * The ViewHolder of this Adapter, useful to store e recycle element for performance reasons.
     */
    public class ViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ImageView photo;
        private final TextView title;
        private final TextView category;
        @Getter private Coupon coupon;

        public ViewHolder(View itemView) {
            super(itemView, mMultiSelector);

            photo = (ImageView) itemView.findViewById(R.id.coupon_entry_photo);
            title = (TextView) itemView.findViewById(R.id.coupon_entry_title);
            category = (TextView) itemView.findViewById(R.id.coupon_entry_category);

            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Coupon coupon) {
            this.coupon = coupon;

            photo.setImageBitmap(BitmapFactory.decodeByteArray(coupon.getPhoto(), 0, coupon.getPhoto().length));
            title.setText(coupon.getTitle());
            category.setText(coupon.getCategory());
        }

        @Override
        public void onClick(View v) {
            clickListener.clickOnItem(this);
        }


        @Override
        public boolean onLongClick(View v) {
            clickListener.longClickOnItem(this);
            return true;
        }
    }




}