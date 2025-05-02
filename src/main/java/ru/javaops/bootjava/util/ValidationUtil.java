package ru.javaops.bootjava.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.javaops.bootjava.model.AbstractBaseEntity;
import ru.javaops.bootjava.util.exception.NotFoundException;

import ru.javaops.bootjava.util.exception.ErrorType;

public class ValidationUtil {
    public static <T> T checkNotFound(T object, int id) {
        checkNotFound(object != null, id);
        return object;
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
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

    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logStackTrace) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }
}
