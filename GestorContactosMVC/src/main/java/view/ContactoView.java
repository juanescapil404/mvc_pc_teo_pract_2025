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