package com.trianasalesianos.dam.ArmarioVirtual;

import com.trianasalesianos.dam.ArmarioVirtual.model.Admin;
import com.trianasalesianos.dam.ArmarioVirtual.model.Cliente;
import com.trianasalesianos.dam.ArmarioVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        seedAdmins();
        seedClientes();
    }


    private void seedAdmins() {
        Admin admin1 = Admin.builder()
                .nombre("Admin 1")
                .username("admin1")
                .password("admin123")
                .email("admin1@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();
        usuarioRepository.save(admin1);

        Admin admin2 = Admin.builder()
                .nombre("Admin 2")
                .username("admin2")
                .password("admin123")
                .email("admin2@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();
        usuarioRepository.save(admin2);

        Admin admin3 = Admin.builder()
                .nombre("Admin 3")
                .username("admin3")
                .password("admin123")
                .email("admin3@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();
        usuarioRepository.save(admin3);
    }

    private void seedClientes() {
        Cliente cliente1 = Cliente.builder()
                .nombre("Juan Pérez")
                .username("juanp")
                .password("password123")
                .email("juan.perez@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();
        usuarioRepository.save(cliente1);

        Cliente cliente2 = Cliente.builder()
                .nombre("Ana Gómez")
                .username("anag")
                .password("password123")
                .email("ana.gomez@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();
        usuarioRepository.save(cliente2);

        Cliente cliente3 = Cliente.builder()
                .nombre("Carlos López")
                .username("carlosl")
                .password("password123")
                .email("carlos.lopez@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();
        usuarioRepository.save(cliente3);
    }
}
