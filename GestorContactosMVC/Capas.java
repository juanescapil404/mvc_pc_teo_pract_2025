// Main.java
package main;

import view.ContactoView;

public class Main {
    public static void main(String[] args) {
        new ContactoView();
    }
}

// controller/ContactoController.java
// Controlador: ContactoController.java (intermediario entre Vista y Servicio).
package controller;

import model.Contacto;
import service.ContactoService;

import java.util.List;

public class ContactoController {
    private final ContactoService service = new ContactoService();

    public void agregarContacto(String nombre, String email, String telefono) {
        service.agregarContacto(nombre, email, telefono);
    }

    public List<Contacto> obtenerContactos() {
        return service.obtenerContactos();
    }

    public void eliminarContacto(Contacto c) {
        service.eliminarContacto(c);
    }
}

// model/Contacto.java
// Modelo y DAO: Contacto.java y ContactoDAO.java (acceso a la base de datos).
package model;

public class Contacto {
    private int id;
    private String nombre;
    private String email;
    private String telefono;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}

// model/ContactoDAO.java
// Modelo y DAO: Contacto.java y ContactoDAO.java (acceso a la base de datos).
package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactoDAO {
    private final String URL = "jdbc:mysql://localhost:3306/contactosdb";
    private final String USER = "root";
    private final String PASSWORD = "tu_contraseña";

    public void guardar(Contacto c) {
        String sql = "INSERT INTO contactos (nombre, email, telefono) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getEmail());
            stmt.setString(3, c.getTelefono());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contacto> listar() {
        List<Contacto> lista = new ArrayList<>();
        String sql = "SELECT * FROM contactos";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Contacto c = new Contacto();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setEmail(rs.getString("email"));
                c.setTelefono(rs.getString("telefono"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void eliminar(Contacto c) {
        String sql = "DELETE FROM contactos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// service/ContactoService.java
// Servicio: ContactoService.java (reglas de negocio).
package service;

import model.Contacto;
import model.ContactoDAO;

import java.util.List;

public class ContactoService {
    private final ContactoDAO dao = new ContactoDAO();

    public void agregarContacto(String nombre, String email, String telefono) {
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre requerido");
        Contacto c = new Contacto();
        c.setNombre(nombre);
        c.setEmail(email);
        c.setTelefono(telefono);
        dao.guardar(c);
    }

    public List<Contacto> obtenerContactos() {
        return dao.listar();
    }

    public void eliminarContacto(Contacto c) {
        dao.eliminar(c);
    }
}

// view/ContactoView.java
// Vista: ContactoView.java (interfaz Swing).
package view;

import controller.ContactoController;
import model.Contacto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContactoView extends JFrame {
    private ContactoController controller = new ContactoController();
    private JTextArea area;

    public ContactoView() {
        super("Gestor de Contactos");
        setLayout(new BorderLayout());

        JButton botonMostrar = new JButton("Mostrar Contactos");
        area = new JTextArea(10, 30);
        JButton botonAgregar = new JButton("Agregar Ejemplo");

        botonMostrar.addActionListener(e -> mostrarContactos());
        botonAgregar.addActionListener(e -> {
            controller.agregarContacto("Juan", "juan@mail.com", "123456");
            mostrarContactos();
        });

        add(botonAgregar, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
        add(botonMostrar, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void mostrarContactos() {
        List<Contacto> contactos = controller.obtenerContactos();
        area.setText("");
        for (Contacto c : contactos) {
            area.append(c.getNombre() + " - " + c.getEmail() + " - " + c.getTelefono() + "\n");
        }
    }
}

// ContactoDAOTest.java
package model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactoDAOTest {
    @Test
    public void testGuardarYListarContacto() {
        ContactoDAO dao = new ContactoDAO();
        Contacto c = new Contacto();
        c.setNombre("Test");
        c.setEmail("test@mail.com");
        c.setTelefono("999999");
        dao.guardar(c);

        List<Contacto> lista = dao.listar();
        assertTrue(lista.stream().anyMatch(cont -> cont.getEmail().equals("test@mail.com")));
    }
}
