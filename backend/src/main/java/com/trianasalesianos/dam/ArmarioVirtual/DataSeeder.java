package com.trianasalesianos.dam.ArmarioVirtual;

import com.trianasalesianos.dam.ArmarioVirtual.model.*;
import com.trianasalesianos.dam.ArmarioVirtual.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private ConjuntoRepository conjuntoRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private TipoPrendaRepository tipoPrendaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            seedAdmins();
            seedClientes();
            seedPrendasYConjuntos();
            seedComentarios();
        }
    }

    private List<Cliente> clientes;
    private List<TipoPrenda> tipoPrendas;
    private List<Admin> admins;
    private List<Tag> tags;

    private void seedAdmins() {
        Admin admin1 = Admin.builder()
                .nombre("Admin 1")
                .username("admin1")
                .password(passwordEncoder.encode("admin123"))
                .email("admin1@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();

        Admin admin2 = Admin.builder()
                .nombre("Admin 2")
                .username("admin2")
                .password(passwordEncoder.encode("admin123"))
                .email("admin2@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();

        admins = usuarioRepository.saveAll(List.of(admin1, admin2));
    }

    private void seedClientes() {
        Cliente cliente1 = Cliente.builder()
                .nombre("Juan Pérez")
                .username("juanp")
                .password(passwordEncoder.encode("password123"))
                .email("juan.perez@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();

        Cliente cliente2 = Cliente.builder()
                .nombre("Ana Gómez")
                .username("anag")
                .password(passwordEncoder.encode("password123"))
                .email("ana.gomez@email.com")
                .fechaRegistro(LocalDateTime.now())
                .enable(true)
                .activo(true)
                .build();

        clientes = usuarioRepository.saveAll(List.of(cliente1, cliente2));
    }

    private Tag findOrCreateTag(String nombre) {
        return tagRepository.findByNombre(nombre)
                .orElseGet(() -> tagRepository.save(Tag.builder().nombre(nombre).build()));
    }

    private void seedPrendasYConjuntos() {
        if (clientes.isEmpty()) return;

        TipoPrenda camiseta = tipoPrendaRepository.findByNombre("Camiseta");
        if (camiseta == null) {
            camiseta = TipoPrenda.builder()
                    .nombre("Camiseta")
                    .build();
            tipoPrendaRepository.save(camiseta);
        }

        TipoPrenda pantalon = tipoPrendaRepository.findByNombre("Pantalón");
        if (pantalon == null) {
            pantalon = TipoPrenda.builder()
                    .nombre("Pantalón")
                    .build();
            tipoPrendaRepository.save(pantalon);
        }

        TipoPrenda chaqueta = tipoPrendaRepository.findByNombre("Chaqueta");
        if (chaqueta == null) {
            chaqueta = TipoPrenda.builder()
                    .nombre("Chaqueta")
                    .build();
            tipoPrendaRepository.save(chaqueta);
        }

        TipoPrenda zapatos = tipoPrendaRepository.findByNombre("Zapatos");
        if (zapatos == null) {
            zapatos = TipoPrenda.builder()
                    .nombre("Zapatos")
                    .build();
            tipoPrendaRepository.save(zapatos);
        }

        TipoPrenda sombrero = tipoPrendaRepository.findByNombre("Sombrero");
        if (sombrero == null) {
            sombrero = TipoPrenda.builder()
                    .nombre("Sombrero")
                    .build();
            tipoPrendaRepository.save(sombrero);
        }

        TipoPrenda camisetaMangaLarga = TipoPrenda.builder()
                .nombre("Camiseta de Manga Larga")
                .tipoPrendaPadre(camiseta)
                .build();
        tipoPrendaRepository.save(camisetaMangaLarga);

        TipoPrenda camisetaDeportiva = TipoPrenda.builder()
                .nombre("Camiseta Deportiva")
                .tipoPrendaPadre(camiseta)
                .build();
        tipoPrendaRepository.save(camisetaDeportiva);

        TipoPrenda jeans = TipoPrenda.builder()
                .nombre("Jeans")
                .tipoPrendaPadre(pantalon)
                .build();
        tipoPrendaRepository.save(jeans);

        TipoPrenda chaquetaCuero = TipoPrenda.builder()
                .nombre("Chaqueta de Cuero")
                .tipoPrendaPadre(chaqueta)
                .build();
        tipoPrendaRepository.save(chaquetaCuero);

        TipoPrenda botines = TipoPrenda.builder()
                .nombre("Botines")
                .tipoPrendaPadre(zapatos)
                .build();
        tipoPrendaRepository.save(botines);

        Tag elegante       = findOrCreateTag("elegante");
        Tag gotico        = findOrCreateTag("gótico");
        Tag urbano        = findOrCreateTag("urbano");
        Tag deportivo     = findOrCreateTag("deportivo");
        Tag casual        = findOrCreateTag("casual");
        Tag formal        = findOrCreateTag("formal");
        Tag bohemio       = findOrCreateTag("bohemio");
        Tag vintage       = findOrCreateTag("vintage");
        Tag minimalista   = findOrCreateTag("minimalista");
        Tag colorido      = findOrCreateTag("colorido");

        Prenda p1 = Prenda.builder()
                .nombre("Camiseta Roja")
                .imagen("camiseta_roja.jpeg")
                .color("Rojo")
                .talla("M")
                .enlaceCompra("http://tienda.com/camiseta-roja")
                .visibilidad(Visibilidad.PUBLICO)
                .fechaPublicacion(LocalDateTime.now())
                .cliente(clientes.get(0))
                .tipoPrenda(camiseta)
                .build();
        p1.getTags().add(deportivo);
        deportivo.getPrendas().add(p1);

        p1.getTags().add(minimalista);
        minimalista.getPrendas().add(p1);

        Prenda p2 = Prenda.builder()
                .nombre("Pantalón Azul")
                .imagen("pantalon_azul.jpg")
                .color("Azul")
                .talla("L")
                .enlaceCompra("http://tienda.com/pantalon-azul")
                .visibilidad(Visibilidad.PUBLICO)
                .fechaPublicacion(LocalDateTime.now())
                .cliente(clientes.get(1))
                .tipoPrenda(pantalon)
                .build();
        p2.getTags().add(urbano);
        urbano.getPrendas().add(p2);

        prendaRepository.saveAll(List.of(p1, p2));

        Conjunto c1 = Conjunto.builder()
                .nombre("Conjunto Deportivo")
                .imagen("conjunto_deportivo.jpg")
                .fechaPublicacion(LocalDateTime.now())
                .visibilidad(Visibilidad.PUBLICO)
                .cliente(clientes.get(0))
                .build();
        c1.getPrendas().add(p1);
        c1.getTags().add(deportivo);
        deportivo.getConjuntos().add(c1);

        Conjunto c2 = Conjunto.builder()
                .nombre("Conjunto Casual")
                .imagen("conjunto_casual.jpg")
                .fechaPublicacion(LocalDateTime.now())
                .visibilidad(Visibilidad.PUBLICO)
                .cliente(clientes.get(1))
                .build();
        c2.getPrendas().add(p2);
        c2.getTags().add(casual);
        casual.getConjuntos().add(c2);
        c2.getTags().add(colorido);
        colorido.getConjuntos().add(c2);

        conjuntoRepository.saveAll(List.of(c1, c2));
    }

    private void seedComentarios() {
        if (clientes.isEmpty() || prendaRepository.count() == 0) return;

        Comentario comentario1 = Comentario.builder()
                .contenido("Me encanta esta camiseta roja, la compré y es de excelente calidad.")
                .fechaPublicacion(LocalDateTime.now())
                .cliente(clientes.get(0))
                .prenda(prendaRepository.findAll().get(0))
                .build();

        Comentario comentario2 = Comentario.builder()
                .contenido("El pantalón azul es perfecto para cualquier ocasión, lo recomiendo mucho.")
                .fechaPublicacion(LocalDateTime.now())
                .cliente(clientes.get(1))
                .prenda(prendaRepository.findAll().get(1))
                .build();

        comentarioRepository.saveAll(List.of(comentario1, comentario2));
    }

}
