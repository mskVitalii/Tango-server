package com.tango.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvitationRequest {
    long userId;
    String rights;
    String role;
}
