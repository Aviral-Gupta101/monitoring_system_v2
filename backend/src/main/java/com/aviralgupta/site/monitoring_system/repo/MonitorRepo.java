package com.aviralgupta.site.monitoring_system.repo;

import com.aviralgupta.site.monitoring_system.entity.Monitor;
import com.aviralgupta.site.monitoring_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitorRepo extends JpaRepository<Monitor, String> {
    List<Monitor> findAllByUser(User user);
}
