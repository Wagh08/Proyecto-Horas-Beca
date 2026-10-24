package com.horasbeca.model;

public record Usuario(long id, String correo, String contrasenaHash, String rol) {
}