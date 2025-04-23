package controller;

import model.Contacto;
import model.ContactoDAO;

import java.util.List;

public class ContactoController {
    private final ContactoDAO dao = new ContactoDAO();

    public void agregarContacto(String nombre, String email, String telefono) {
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