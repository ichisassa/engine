package com.mingshiu.engine.service.resonator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * ResonatorService Class
 */
@Service
@RequiredArgsConstructor
public class ResonatorService {

  private static final Logger log = LoggerFactory.getLogger(ResonatorService.class);

  public void showDisp(Model model, HttpSession session) {
    return;
  }
}
