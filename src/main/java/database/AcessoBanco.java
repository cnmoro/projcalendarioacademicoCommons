package database;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import models.Evento;
import models.Loginscricao;
import models.Participacao;
import models.Profatendimento;
import models.Reuniaoprofessor;
import models.Usuario;
import utils.MD5Util;

/**
 *
 * @author moro
 */
public class AcessoBanco {

    private final EntityManager manager;
    private final Object operationLock;
    public final static long DOIS_DIAS = 48 * 60 * 60 * 1000L;
    
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
    
    public List<Reuniaoprofessor> getReuniaoByProf(Usuario professor) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Reuniaoprofessor.findByProf").setParameter("idprofessor", professor).getResultList();
        }
    }
    
    public List<Reuniaoprofessor> getReuniaoByUsuario(Usuario usuario) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Reuniaoprofessor.findByUser").setParameter("idusuario", usuario).getResultList();
        }
    }
    
    public Reuniaoprofessor getReuniaoById(int id) {
        synchronized (this.operationLock) {
            return (Reuniaoprofessor) this.manager.createNamedQuery("Reuniaoprofessor.findById", Reuniaoprofessor.class).setParameter("id", id).getSingleResult();
        }
    }
    
    public void removeReuniao(Reuniaoprofessor rp) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.remove(this.manager.createNamedQuery("Reuniaoprofessor.findById").setParameter("id", rp.getId()));
            this.manager.getTransaction().commit();
        }
    }
    
    public void cadastraReuniao(Reuniaoprofessor rp) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(rp);
            this.manager.getTransaction().commit();
        }
    }
    
    public void updateReuniao(Reuniaoprofessor rp) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(rp);
            this.manager.getTransaction().commit();
        }
    }
    
    public void updateParticipacao(Participacao p) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(p);
            this.manager.getTransaction().commit();
        }
    }
    
    public List<Participacao> getParticipacoes() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Participacao.findAll").getResultList();
        }
    }
    
    public List<Participacao> getParticipacoesByUsuario(Usuario u) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Participacao.findByUsuario").setParameter("idusuario", u).getResultList();
        }
    }
    
    public void cadastraProfatendimento(Profatendimento pa) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.persist(pa);
            this.manager.getTransaction().commit();
        }
    }
    
    public void updateProfatendimento(Profatendimento pa) {
        synchronized (this.operationLock) {
            this.manager.getTransaction().begin();
            this.manager.merge(pa);
            this.manager.getTransaction().commit();
        }
    }
    
    public List<Profatendimento> getProfatendimentos() {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Profatendimento.findAll").getResultList();
        }
    }
    
    public List<Profatendimento> getProfatendimentosByProfessor(Usuario professor) {
        synchronized (this.operationLock) {
            return this.manager.createNamedQuery("Profatendimento.findByProfessor").setParameter("idprofessor", professor).getResultList();
        }
    }
    
    public String getProximosEventos() {
        synchronized (this.operationLock) {
            List<Evento> eventos = getEventos();
            StringBuilder sbuilder = new StringBuilder();
            Date horaAtual = new Date();
            for (int i = 0; i < eventos.size(); i++) {
                if (Math.abs(horaAtual.getTime() - eventos.get(i).getDatainicio().getTime()) <= DOIS_DIAS) {
                    sbuilder.append(eventos.get(i).getNome()).append(", ").append(eventos.get(i).getHoras()).append(" horas complementares.");
                    sbuilder.append(System.getProperty("line.separator"));
                }
            }
            return sbuilder.toString();
        }
    }
}
