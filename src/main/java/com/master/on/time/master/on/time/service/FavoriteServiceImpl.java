package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.dto.FavoriteDto;
import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.exception.UserRoleNotAllowedException;
import com.master.on.time.master.on.time.model.Favorite;
import com.master.on.time.master.on.time.model.RoleName;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.FavoriteRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void addToFavorites(Long specialistId) {
        Long userId = userService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Authenticated user not found with id: "
                        + userId));

        User specialist = userRepository.findById(specialistId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Specialist not found with id: "
                        + specialistId));

        if (!isSpecialist(specialist)) {
            throw new UserRoleNotAllowedException("Target user is not a specialist");
        }

        boolean alreadyFavorited = favoriteRepository
                .findByUserAndTarget(user, specialist)
                .isPresent();
        if (!alreadyFavorited) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setTarget(specialist);
            favoriteRepository.save(favorite);
        }
    }

    @Override
    public void removeFromFavorites(Long specialistId) {
        Long clientId = userService.getAuthenticatedUserId();
        User client = userRepository.findById(clientId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Authenticated user not found with id: "
                        + clientId));

        User specialist = userRepository.findById(specialistId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Specialist not found with id: "
                        + specialistId));

        favoriteRepository.deleteByUserAndTarget(client, specialist);
    }

    @Override
    public List<FavoriteDto> getFavorites() {
        Long userId = userService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Authenticated user not found with id: "
                        + userId));

        return favoriteRepository.findByUser(user).stream()
                .map(fav -> new FavoriteDto(
                        fav.getTarget().getId(),
                        fav.getTarget().getFirstName()
                                + " "
                                + fav.getTarget().getLastName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<FavoriteDto> getFavoritesByClientId(Long clientId) {
        User user = userRepository.findById(clientId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id: "
                        + clientId));

        return favoriteRepository.findByUser(user).stream()
                .map(fav -> new FavoriteDto(
                        fav.getTarget().getId(),
                        fav.getTarget().getFirstName()
                                + " "
                                + fav.getTarget().getLastName()
                ))
                .collect(Collectors.toList());
    }

    private boolean isSpecialist(User user) {
        Set<RoleName> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
        return roles.contains(RoleName.SPECIALIST);
    }
}
