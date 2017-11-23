package Utils;

import business.EnviaEmail;
import database.AcessoBanco;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author cnmoro
 */
public class EManager implements java.io.Serializable{

    private static final Object singletonLock = new Object();
    private static EManager instance = null;

    private final EntityManager em;

    private final Object operationLock = new Object();
    private final AcessoBanco databaseAccessor;
    private final EnviaEmail enviaEmailAccessor;
    
    public static EManager getInstance() {
        if (instance == null) {
            synchronized (singletonLock) {
                if (instance == null) {
                    instance = new EManager();
                }
            }
        }
        return instance;
    }

    private EManager() {
        this.em = Persistence.createEntityManagerFactory("CalendarioPU").createEntityManager();
        this.databaseAccessor = new AcessoBanco(this.em, this.operationLock);
        this.enviaEmailAccessor = new EnviaEmail(this.em, operationLock);
    }

    public AcessoBanco getDatabaseAccessor() {
        return databaseAccessor;
    }

    public EnviaEmail getEnviaEmailAccessor() {
        return enviaEmailAccessor;
    }
    
}