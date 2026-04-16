package com.food.khaaonow.repo;

import com.food.khaaonow.model.restaurant.Restaurant;
import com.food.khaaonow.service.RestaurantNearbyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    @Query(value = """
    SELECT r.id, r.name,r.status,r.restaurant_image,
           a.state, a.street, a.city,
           (
               6371 * acos(
                   cos(radians(:lat)) * cos(radians(a.latitude)) *
                   cos(radians(a.longitude) - radians(:lng)) +
                   sin(radians(:lat)) * sin(radians(a.latitude))
               )
           ) AS distance
    FROM restaurants r
    JOIN restaurant_addresses ra
      ON r.restaurant_address_id = ra.id
    JOIN addresses a
      ON ra.address_id = a.id
 WHERE a.latitude BETWEEN :minLat AND :maxLat
      AND a.longitude BETWEEN :minLng AND :maxLng
    HAVING distance <= :radius
    ORDER BY distance
""", nativeQuery = true)
    List<RestaurantNearbyDTO> findNearbyRestaurants(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius,
            @Param("minLat")double minLat,
            @Param("maxLat")double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng
    );

//    Optional<Restaurant> findByRestaurantByIdAndOwnerId(Long restaurantid, Long ownerid);
}
