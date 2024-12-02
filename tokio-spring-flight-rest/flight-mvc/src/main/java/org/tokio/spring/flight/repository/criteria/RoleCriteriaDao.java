package org.tokio.spring.flight.repository.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tokio.spring.flight.domain.Role;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleCriteriaDao {

    private final EntityManager em;

    public Optional<Role> findRoleByName(String name) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        // tipo de objeto de la query   (control de tipo)
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        // tipo de objeto que devuelve
        Root<Role> roleRoot = criteriaQuery.from(Role.class);
        // filter el resutlado por
        criteriaQuery.where(criteriaBuilder.equal(roleRoot.get("name"), name));

        // ejecuta query
        try{
            final Role role = em.createQuery(criteriaQuery).getSingleResult();
            return Optional.of(role);
        }catch (NoResultException ex){
            log.error("Don't result, "+ex);
            return  Optional.empty();
        }
    }

}
