package com.couponduniatask.network.Response;

import java.util.List;

public class GetResponse {


    public static class Data {
        public String SubFranchiseID;
        public String OutletID;
        public String OutletName;
        public String BrandID;
        public String Address;
        public String NeighbourhoodID;
        public String CityID;
        public String Email;
        public String Timings;
        public String CityRank;
        public String Latitude;
        public String Longitude;
        public String Pincode;
        public String Landmark;
        public String Streetname;
        public String BrandName;
        public String OutletURL;
        public String NumCoupons;
        public String NeighbourhoodName;
        public String PhoneNumber;
        public String CityName;
        public String Distance;
        public List<Category> Categories;
        public String LogoURL;
        public String CoverURL;

    }


    public static class ApiResponse {
        public List<GetResponse.Data> data;

    }

    public static class Category {
        public String OfflineCategoryID;
        public String Name;
        public String ParentCategoryID;
        public String CategoryType;
    }

}
