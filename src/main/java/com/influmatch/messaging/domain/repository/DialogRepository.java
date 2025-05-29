package com.influmatch.messaging.domain.repository;

import com.influmatch.messaging.domain.model.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogRepository extends JpaRepository<Dialog, Long> {}
