package ru.javaops.bootjava.util;

import org.springframework.lang.NonNull;
import ru.javaops.bootjava.model.AbstractBaseEntity;
import ru.javaops.bootjava.model.Menu;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.util.exception.NotFoundException;

import java.time.LocalDate;

public class ValidationUtil {
    public static <T> T checkNotFound(T object, int id) {
        checkNotFound(object != null, id);
        return object;
    }

    public static void checkNotFound(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkIsNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(@NonNull AbstractBaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else {
            Integer entityId = entity.getId();
            if (entityId != null && entityId != id) {
                throw new IllegalArgumentException(entity + " must be with id=" + id);
            }
        }
    }

    public static void assureMenuRestaurantIdAndDateConsistent(@NonNull Menu menu, int restaurantId, LocalDate menuDate) {
        Restaurant restaurant = menu.getRestaurant();
        if (restaurant == null) {
            throw new IllegalArgumentException(menu + " restaurant must be not null");
        }
        Integer menuRestaurantId = restaurant.getId();
        if (menuRestaurantId != null && menuRestaurantId != restaurantId) {
            throw new IllegalArgumentException(menu + " must be with restaurantId=" + restaurantId);
        }
        if (menu.getMenuDate() != menuDate) {
            throw new IllegalArgumentException(menu + " must be with menu_date=" + menuDate.toString());
        }
    }

    public static void assureIdAuthorized(int id) {
        int userId = SecurityUtil.authUserId();
        if (id != userId) {
            throw new IllegalArgumentException("id " + id + " does not match authorized user");
        }
    }

}
