package it.polimi.spf.demo.couponing.client.coupons;

import java.util.ArrayList;
import java.util.List;

import it.polimi.spf.demo.couponing.client.Coupon;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 22/06/15.
 */
public class CouponList {

    @Getter private final List<Coupon> couponList;


    private static final CouponList instance = new CouponList();

    /**
     * Method to get the instance of this class.
     *
     * @return instance of this class.
     */
    public static CouponList getInstance() {
        return instance;
    }

    /**
     * Private constructor, because is a singleton class.
     */
    private CouponList() {
        couponList = new ArrayList<>();
    }
}