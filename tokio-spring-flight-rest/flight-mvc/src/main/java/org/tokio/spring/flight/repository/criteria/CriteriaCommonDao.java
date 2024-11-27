package org.tokio.spring.flight.repository.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CriteriaCommonDao {

    private final EntityManager em;

    /**
     * This method generic by checked the value of a filed for a table of bbdd
     *
     * @param nameField name field of table
     * @param valueField value by filter
     * @param clazz class of entity that model the table
     * @return true if there are elements, otherwise false
     *
     * @param <T> type entity
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public <T> boolean exitsByField(String nameField,Object valueField,Class<T> clazz){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // tipo de objeto de la query   (control de tipo)
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        // tipo de objeto que devuelve
        Root<T> roleRoot = criteriaQuery.from(clazz);
        // filter el resutlado por
        criteriaQuery.where(criteriaBuilder.equal(roleRoot.get(nameField), valueField));
        // ejecuta query
        try{
            final T result = em.createQuery(criteriaQuery).getSingleResult();
            return result != null;
        }catch (NoResultException ex){
            log.error("Don't result, "+ex);
            return  false;
        }
    }

    /**
     * This method generic that return row
     *
     * @param nameField name field of table
     * @param valueField value by filter
     * @param clazz class of entity that model the table
     * @return optional empty if there aren't element, optional with data
     *
     * @param <T> type entity
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public <T> Optional<T> getRowByField(String nameField,Object valueField,Class<T> clazz){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // tipo de objeto de la query   (control de tipo)
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        // tipo de objeto que devuelve
        Root<T> roleRoot = criteriaQuery.from(clazz);
        // filter el resutlado por
        criteriaQuery.where(criteriaBuilder.equal(roleRoot.get(nameField), valueField));
        // ejecuta query
        try{
            final T result = em.createQuery(criteriaQuery).getSingleResult();
            return Optional.of(result);
        }catch (NoResultException ex){
            log.error("Don't result, "+ex);
            return  Optional.empty();
        }
    }
}
