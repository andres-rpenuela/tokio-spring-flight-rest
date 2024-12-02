package org.tokio.spring.flight.repository.jdcb;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.tokio.spring.flight.domain.Role;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleJdbcDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public int countRoles(){
        // query native sql
        final String query = "SELECT COUNT(1) AS count FROM roles";

        // mapper a row to Object
        RowMapper<Integer> counterRowMapper =(resultSet, rowNumber) -> resultSet.getInt("count");
        // execute query
        return namedParameterJdbcTemplate.queryForObject(
                query, // query
                Map.of(), // params
                counterRowMapper // result
        );
    }

    public Long newRole(Role role){

        final Map<String, String> params = Map.of("name",role.getName());

        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("roles")
                .usingGeneratedKeyColumns("id");

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<Role> findRoleById(Long roleId){

        // best practice: nombrar los campos a recuperar en la query para evitar EmptyResultDataAccessException.class
        final String query = "SELECT id, name FROM roles WHERE id = :id";

        final RowMapper<Role> roleRowMapper = (rs, rowNum) ->
                Role.builder().id(rs.getLong("id")).name(rs.getString("name")).build();

        try {
            final Role role = namedParameterJdbcTemplate.queryForObject(
                    query,
                    Map.of("id", roleId),
                    roleRowMapper
            );
            return Optional.ofNullable(role);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }

    // base of sql injection
    public int deleteRoleByName(String name){
        final String query = "DELETE FROM roles WHERE name = '%s'".formatted(name);

        // solved Option A: "DELETE FROM roles WHERE name :nameRole ";    and Map.of("nameRole",name)
        // solver Option B: Use api as JPA, that wrapper this functions

        return namedParameterJdbcTemplate.update(query,Map.of());
    }
}
