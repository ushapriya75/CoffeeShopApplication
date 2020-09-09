package com.org.coffeeshop.ApiCall;

import com.org.coffeeshop.modal.ZometoResponse;
import com.org.coffeeshop.modal.nearbyResturants.NearbyResturants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZomatoApi {

    /**
     *
     * @param entityId entity id like 91 for dublin
     * @param entityType entity type like city
     * @param count number of record you want
     * @param sortBy sort ny rating etc
     * @param sortOrder sort order like desc
     * @return
     */
    @GET("api/v2.1/search")
    Call<ZometoResponse> getRestaurants(@Query("entity_id") String entityId,
                                        @Query("entity_type") String entityType, @Query("count") String count,@Query("sort") String sortBy,@Query("order") String sortOrder,@Query("q") String query);

    @GET("api/v2.1/geocode")
    Call<NearbyResturants> getNearByResturants(@Query("lat") Double latitude, @Query("lon") Double longitude);

}