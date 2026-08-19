package spring.security.service;

import spring.security.entity.Users;

public record RotatedRefreshToken(String value, Users user) {
}
