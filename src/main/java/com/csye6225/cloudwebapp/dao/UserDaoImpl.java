package com.csye6225.cloudwebapp.dao;

import com.csye6225.cloudwebapp.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    private EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(User theUser){
        entityManager.persist(theUser);
    }

    @Override
    public List<User> findByEmail(String theEmail) {
        TypedQuery<User> theQuery = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email=:theData", User.class);
        theQuery.setParameter("theData", theEmail);
        return theQuery.getResultList();
    }
}
