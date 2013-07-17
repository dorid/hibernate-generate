package ${classFullName};

import com.synnex.dao3.cloud.GenericVIHibernateDaoImpl;
import ${classFullName}.${className};

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.jboss.annotation.ejb.Clustered;
import org.jboss.annotation.ejb.LocalBinding;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = ${className}DAO.BEAN_NAME)
@LocalBinding(jndiBinding = ${className}DAO.LOCAL)
@Clustered
@Local({${className}DAO.class})
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@PermitAll
public class ${className}DAOBean extends GenericVIHibernateDaoImpl<${className}, Integer> implements <${className}DAO {
    private static final Logger logger = Logger.getLogger(${className}DAOBean.class);

    @PersistenceContext(unitName = "entityManagerCS")
        public void setEntityManager(EntityManager em) {
    super.setEntityManager(em);
    }

}
