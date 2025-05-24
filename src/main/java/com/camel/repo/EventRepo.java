package com.camel.repo;

import com.camel.entity.EventData;
import com.camel.entity.EventDataPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kansanja on 09/02/22.
 */
public interface EventRepo extends JpaRepository<EventData, EventDataPK> {
}
