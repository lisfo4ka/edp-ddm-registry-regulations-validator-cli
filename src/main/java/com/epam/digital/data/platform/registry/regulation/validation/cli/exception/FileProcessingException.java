/*
 * Copyright 2023 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.digital.data.platform.registry.regulation.validation.cli.exception;

import java.io.File;
import lombok.Getter;

public class FileProcessingException extends RuntimeException {

  @Getter
  private File file;

  public FileProcessingException(String message, File file, Exception cause) {
    super(message, cause);
    this.file = file;
  }

  public FileProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
