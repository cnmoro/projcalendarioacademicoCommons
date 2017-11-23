package database;

import java.util.List;
import javax.persistence.EntityManager;
import models.Usuario;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import utils.MD5Util;

/**
 *
 * @author moro
 */
public class DatabaseAccessor {

    private final EntityManager manager;
    private final Object operationLock;

    public DatabaseAccessor(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Usuario> getUsuarios() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findAll").getResultList();
        }
    }
    
    public List<Usuario> getAdmins() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findAdmin", Usuario.class).getResultList();
        }
    }
    
    public List<Usuario> getUsuariosByLoginSenha(String username, String password) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findByLoginSenha", Usuario.class).setParameter("login", username).setParameter("senha", MD5Util.md5Hash(password)).getResultList();
        }
    }
    
    public Usuario getUsuarioByLoginEmail(String login, String email) {
        synchronized (this.operationLock) {
            return (Usuario) this.manager.createNamedQuery("Usuario.findByLoginEmail", Usuario.class).setParameter("login", login).setParameter("email", email).getSingleResult();
        }
    }
    
    public Usuario getUsuarioByLoginEmailCodigo(String login, String email, String codigo) {
        synchronized (this.operationLock) {
            return (Usuario) this.manager.createNamedQuery("Usuario.findByLoginEmailCodigo").setParameter("login", login).setParameter("email", email).setParameter("codigorecuperacao", codigo).getSingleResult();
        }
    }
    
    public void cadastraUsuario(Usuario u) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(u);
            this.manager.getTransaction().commit();
        }
    }
    
    public void updateUsuario(Usuario u) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(u);
            this.manager.getTransaction().commit();
        }
    }
    
    public void removeUsuario(Usuario u) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.remove(this.manager.createNamedQuery("Usuario.findById").setParameter("id", u.getId()));
            this.manager.getTransaction().commit();
        }
    }
    
}
