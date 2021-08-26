package com.epam.digital.data.platform.registry.regulation.validation.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SystemExit {

  private static final int SUCCESS_EXIT_CODE = 0;
  private static final int SYSTEM_ERROR_EXIT_CODE = 1;
  private static final int VALIDATION_FAILURE_EXIT_CODE = 10;

  private final ApplicationContext context;

  public SystemExit(ApplicationContext context) {
    this.context = context;
  }

  public void complete() {
    exit(SUCCESS_EXIT_CODE);
  }

  public void validationFailure() {
    exit(VALIDATION_FAILURE_EXIT_CODE);
  }

  public void systemError() {
    exit(SYSTEM_ERROR_EXIT_CODE);
  }

  private void exit(int exitCode) {
    var code = SpringApplication.exit(context, () -> exitCode);
    System.exit(code);
  }
}
