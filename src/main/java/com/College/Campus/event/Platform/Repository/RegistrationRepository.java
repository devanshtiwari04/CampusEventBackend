package com.College.Campus.event.Platform.Repository;

import com.College.Campus.event.Platform.Entity.Event;
import com.College.Campus.event.Platform.Entity.Registration;
import com.College.Campus.event.Platform.Entity.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    static void deleteByEventId(Long id) {
    }

    List<Registration> findByEventAndStatus(Event event, RegistrationStatus status);

    List<Registration> findByUserId(Long userId);

    List<Registration> findByStatus(RegistrationStatus registrationStatus);
}
