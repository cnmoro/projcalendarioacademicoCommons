package database;

import java.util.List;
import javax.persistence.EntityManager;
import models.Evento;
import models.Loginscricao;
import models.Participacao;
import models.Usuario;
import utils.MD5Util;

/**
 *
 * @author moro
 */
public class AcessoBanco {

    private final EntityManager manager;
    private final Object operationLock;

    public AcessoBanco(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }

    public List<Usuario> getUsuarios() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findAll").getResultList();
        }
    }
    
    public List<Usuario> getProfessores() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findProfessores").getResultList();
        }
    }
    
    public List<Usuario> getUsuarioByLogin(String login) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findByLogin").setParameter("login", login).getResultList();
        }
    }
    
    public List<Usuario> getUsuariosByNivelAcesso() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Usuario.findByNivelacesso").getResultList();
        }
    }
    
    public Usuario getUsuarioById(int id) {
        synchronized (this.operationLock) {
            return (Usuario) this.manager.createNamedQuery("Usuario.findById").setParameter("id", id).getSingleResult();
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
    
    public List<Evento> getEventos() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Evento.findAll").getResultList();
        }
    }
    
    public List<Evento> getEventosByAutor(String autor) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Evento.findByAutor").setParameter("autor", autor).getResultList();
        }
    }
    
    public void cadastraEvento(Evento e) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(e);
            this.manager.getTransaction().commit();
        }
    }
    
    public void updateEvento(Evento e) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(e);
            this.manager.getTransaction().commit();
        }
    }
    
    public void removeEvento(Evento e) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.remove(this.manager.createNamedQuery("Evento.findById").setParameter("id", e.getId()));
            this.manager.getTransaction().commit();
        }
    }
    
    public void cadastraParticipacao(Participacao p) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(p);
            this.manager.getTransaction().commit();
        }
    }
    
    public void cadastraLogInscricao(Loginscricao log) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(log);
            this.manager.getTransaction().commit();
        }
    }
}
