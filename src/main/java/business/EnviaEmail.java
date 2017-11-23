package business;

import Utils.EManager;
import java.util.List;
import javax.persistence.EntityManager;
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
}
