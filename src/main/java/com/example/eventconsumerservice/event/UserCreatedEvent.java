package com.example.eventconsumerservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {

    private String userId;
    private String email;
    private String profileName;
    private String bio;
}
