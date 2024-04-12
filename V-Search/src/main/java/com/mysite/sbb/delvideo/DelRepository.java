package com.mysite.sbb.delvideo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelRepository extends JpaRepository<Del, Integer> {
    List<Del> findByUsernumber(int usernumber);
}
