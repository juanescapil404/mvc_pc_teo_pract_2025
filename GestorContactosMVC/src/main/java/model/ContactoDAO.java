package model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ContactoDAO {
    public void guardar(Contacto c) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.saveOrUpdate(c);
            tx.commit();
        }
    }

    public List<Contacto> listar() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("from Contacto", Contacto.class).list();
        }
    }

    public void eliminar(Contacto c) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.remove(c);
            tx.commit();
        }
    }
}