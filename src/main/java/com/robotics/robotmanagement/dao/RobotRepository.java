package com.robotics.robotmanagement.dao;

import com.robotics.robotmanagement.dao.entity.Robot;
import com.robotics.robotmanagement.web.dto.PhaseCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("robotRepository")
public interface RobotRepository extends JpaRepository<Robot,Long>,CustomRobotRepository {

   List<Robot> findAll();

    boolean existsByRobotId(String robotId);

    Robot findByRobotId(String robotId);

    @Modifying
    @Query("update Robot r set r.robotPhase = 'DAMAGED_BEYOND_REPAIR' where r.robotId = :robotId")
    int deleteByRobotId(@Param("robotId")String robotId);

    @Query("select new com.robotics.robotmanagement.web.dto.PhaseCountDto(r.robotPhase, count(r.robotPhase)) from Robot r group by r.robotPhase")
    List<PhaseCountDto> countRobotsPerPhase();
}
