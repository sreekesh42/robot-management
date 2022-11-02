package com.robotics.robotmanagement.dao;

import com.robotics.robotmanagement.dao.entity.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomRobotRepositoryImpl implements CustomRobotRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public List<Robot> findAllActiveRobots() {
        String jpql = "select r from Robot r where r.robotPhase != 'DAMAGED_BEYOND_REPAIR'";
        TypedQuery<Robot> query = entityManager.createQuery(jpql, Robot.class);
        return query.getResultList();
    }
}
