/*
 * Copyright 2014 Jacopo Aliprandi, Dario Archetti
 * Copyright 2015 Stefano Cappa
 *
 * This file is part of SPF.
 *
 * SPF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * SPF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SPF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package it.polimi.spf.demo.couponing.client;

import it.polimi.spf.lib.SPFPermissionManager;
import it.polimi.spf.shared.model.Permission;
import android.app.Application;

public class ClientApplication extends Application{

	private static ClientApplication instance;
	private CouponDatabase mDatabase;
	
	public static ClientApplication get(){
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		mDatabase = new CouponDatabase(this);
		
		SPFPermissionManager.get().requirePermission(
				Permission.READ_LOCAL_PROFILE,
				Permission.WRITE_LOCAL_PROFILE,
				Permission.REGISTER_SERVICES,
				Permission.NOTIFICATION_SERVICES);

	}
	
	public CouponDatabase getCouponDatabase(){
		return mDatabase;
	}
}
