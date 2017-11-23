package business;

import utils.EManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import models.Evento;
import models.Usuario;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author moro
 */
public class EnviaEmail {
    
    private final EntityManager manager;
    private final Object operationLock;
    public long DUAS_HORAS = 2 * 60 * 60 * 1000L;
    public long DOIS_DIAS = 48 * 60 * 60 * 1000L;
    public long UM_DIA = 24 * 60 * 60 * 1000L;

    public EnviaEmail(EntityManager manager, Object operationLock) {
        this.manager = manager;
        this.operationLock = operationLock;
    }
    
    public void enviaEmailRecuperacao(String destino, String uuid) throws EmailException {
        synchronized (this.operationLock) {
            Email email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("calendarioeventosbsi@gmail.com", "disciplinabsi"));
            email.setTLS(true);
            email.setFrom("calendarioeventosbsi@gmail.com");
            email.setSubject("Calendário de Eventos - Recuperação de senha");
            email.setMsg("Seu código é: " + uuid);
            email.addTo(destino);
            email.send();
        }
    }
    
    public void emailRequisitaPrivilegio(String nivelAcesso, String login) {
        synchronized (this.operationLock) {
            try {
                Email email = new SimpleEmail();
                email.setHostName("smtp.gmail.com");
                email.setSmtpPort(587);
                email.setAuthenticator(new DefaultAuthenticator("calendarioeventosbsi@gmail.com", "disciplinabsi"));
                email.setTLS(true);
                email.setFrom("calendarioeventosbsi@gmail.com");
                email.setSubject("[ UTFPR - Calendário de Eventos ] Requisição de Acesso");
                email.setMsg("O usuário '" + login
                            + "' está requisitando o privilégio de '" + nivelAcesso + "'.");
                List<Usuario> users = EManager.getInstance().getDatabaseAccessor().getAdmins();
                    if (users.size() > 0) {
                        email.addTo(users.get(0).getEmail());
                        email.send();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void emailAvisaEventos(List<Evento> eventos) {
        synchronized (this.operationLock) {
            try {
                List<Usuario> usuarios = EManager.getInstance().getDatabaseAccessor().getUsuariosByNivelAcesso();
                List<String> emails = new ArrayList<>();

                for (int i = 0; i < usuarios.size(); i++) {
                    emails.add(usuarios.get(i).getEmail());
                }

                Email email = new SimpleEmail();
                email.setHostName("smtp.gmail.com");
                email.setSmtpPort(587);
                email.setAuthenticator(new DefaultAuthenticator("calendarioeventosbsi@gmail.com", "disciplinabsi"));
                email.setTLS(true);
                email.setFrom("calendarioeventosbsi@gmail.com");
                email.setSubject("Calendário de Eventos - Aviso");

                StringBuilder sbuilder = new StringBuilder();
                sbuilder.append("Próximos eventos: ");
                sbuilder.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));

                Date horaAtual = new Date();
                for (int i = 0; i < eventos.size(); i++) {
                    if (Math.abs(horaAtual.getTime() - eventos.get(i).getDatainicio().getTime()) <= UM_DIA) {
                        sbuilder.append(eventos.get(i).getNome()).append(", ").append(eventos.get(i).getHoras()).append(" horas complementares.");
                        sbuilder.append(System.getProperty("line.separator"));
                    }
                }
                sbuilder.append(System.getProperty("line.separator")).append("Saiba mais em nosso site.");

                email.setMsg(sbuilder.toString());

                for (int i = 0; i < emails.size(); i++) {
                    email.addTo(emails.get(i));
                }

                email.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
