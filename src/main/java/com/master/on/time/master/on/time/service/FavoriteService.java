package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.FavoriteDto;
import java.util.List;

public interface FavoriteService {
    void addToFavorites(Long targetId);

    void removeFromFavorites(Long targetId);

    List<FavoriteDto> getFavorites();

    List<FavoriteDto> getFavoritesByClientId(Long clientId);
}
