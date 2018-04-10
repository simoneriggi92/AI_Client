package com.company;

import java.net.CookieManager;
import java.util.ArrayList;

public interface IClient {

   public CookieManager tryToLogin(String username, String password);
   public void postPositions(ArrayList<Position> positionList, CookieManager cookieManager);
   public void getPositions(String startTimestamp, String endTimeStamp, CookieManager cookieManager);
}
