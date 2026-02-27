package com.College.Campus.event.Platform.Controller;


import com.College.Campus.event.Platform.Entity.Event;
import com.College.Campus.event.Platform.Entity.Registration;
import com.College.Campus.event.Platform.Entity.RegistrationStatus;
import com.College.Campus.event.Platform.Entity.User;
import com.College.Campus.event.Platform.Repository.EventRepository;
import com.College.Campus.event.Platform.Repository.RegistrationRepository;
import com.College.Campus.event.Platform.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")

public class RegistrationController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRegistrationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                registrationRepository.findByUserId(userId)
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRegistrations() {

        List<Registration> pending =
                registrationRepository.findByStatus(RegistrationStatus.PENDING);

        return ResponseEntity.ok(pending);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerForEvent(@RequestParam Long userId,
                                              @RequestParam Long eventId) {

        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(eventId).orElse(null);

        if (user == null || event == null) {
            return ResponseEntity.badRequest().body("Invalid User or Event ID");
        }

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setStatus(RegistrationStatus.PENDING);

        return ResponseEntity.ok(registrationRepository.save(registration));
    }
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRegistration(@PathVariable Long id) {

        return registrationRepository.findById(id)
                .map(reg -> {
                    reg.setStatus(RegistrationStatus.APPROVED);
                    return ResponseEntity.ok(registrationRepository.save(reg));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRegistration(@PathVariable Long id) {

        return registrationRepository.findById(id)
                .map(reg -> {
                    reg.setStatus(RegistrationStatus.REJECTED);
                    return ResponseEntity.ok(registrationRepository.save(reg));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/count/{eventId}")
    public ResponseEntity<Integer> getApprovedCount(@PathVariable Long eventId) {

        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Registration> approved =
                registrationRepository.findByEventAndStatus(
                        event, RegistrationStatus.APPROVED);

        return ResponseEntity.ok(approved.size());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegistration(@PathVariable Long id) {

        if (!registrationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registrationRepository.deleteById(id);
        return ResponseEntity.ok("Registration deleted successfully");
    }
}
