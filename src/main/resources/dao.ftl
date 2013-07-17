package ${classFullName};

import com.synnex.dao3.cloud.GenericVIHibernateDao;
import ${classFullName}.${className};


public interface ${className}DAO extends GenericVIHibernateDao<${className}, Integer>{

    String BEAN_NAME = "cloud/billing/${className}DAOBean";
    String LOCAL = "cloud/billing/${className}DAOBeanLocal";

}
