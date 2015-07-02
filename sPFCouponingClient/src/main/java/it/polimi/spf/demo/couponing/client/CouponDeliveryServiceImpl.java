package it.polimi.spf.demo.couponing.client;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import it.polimi.spf.demo.couponing.client.coupons.CouponList;
import it.polimi.spf.lib.services.SPFServiceEndpoint;
import it.polimi.spf.lib.services.ServiceInvocationException;

public class CouponDeliveryServiceImpl extends SPFServiceEndpoint implements CouponDeliveryService {

	private static final Object LOCK = new Object();
	private static final String TAG = "CouponDeliveryService";

	private static CouponListener sCouponHandler;
	private static final CouponListener sDefaultHandler = new NotificationEmitter();

	public interface CouponListener {
		void onCouponReceived(Coupon coupon, Context context);
	}

	public static void setCouponListener(CouponListener listener) {
		synchronized (LOCK) {
			sCouponHandler = listener;
		}
	}

	public static void removeCouponListener(CouponListener listener) {
		synchronized (LOCK) {
			if (sCouponHandler == listener) {
				sCouponHandler = null;
			}
		}
	}


	private boolean checkIfExists (Coupon coupon) {
		for(Coupon couponElem : CouponList.getInstance().getCouponList()) {
			if(couponElem.getTitle().equals(coupon.getTitle()) && couponElem.getCategory().equals(coupon.getCategory()) &&
					couponElem.getText().equals(coupon.getText())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deliverCoupon(Coupon coupon) throws ServiceInvocationException {
		Log.d(TAG, "Incoming coupon: " + coupon);

		//now i'll remove duplicates adding only different coupons in the db
		if(!checkIfExists(coupon)) {
			Log.d(TAG,"Adding coupon: " + coupon);

			ClientApplication.get().getCouponDatabase().saveCoupon(coupon);
			CouponListener listener;
			synchronized (LOCK) {
				listener = sCouponHandler != null ? sCouponHandler : sDefaultHandler;
			}

			listener.onCouponReceived(coupon, this);
		}
	}

	private static class NotificationEmitter implements CouponListener {

		@Override
		public void onCouponReceived(Coupon coupon, Context context) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
            	.setSmallIcon(R.drawable.ic_launcher)
            	.setContentTitle("Coupon received")
            	.setContentText("You received a new coupon for " + coupon.getCategory())
            	.setAutoCancel(true);

            	Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            	mBuilder.setSound(alarmSound);
            	
            	Intent showActivity = new Intent(context, MainActivity.class);
            	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, showActivity, PendingIntent.FLAG_UPDATE_CURRENT);
            	mBuilder.setContentIntent(pendingIntent);

            	int mNotificationId = 0xabc2;
            	NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            	notificationManager.notify(mNotificationId, mBuilder.build());
		}
	}
}
