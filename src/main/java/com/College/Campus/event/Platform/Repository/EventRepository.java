package com.College.Campus.event.Platform.Repository;

import com.College.Campus.event.Platform.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
