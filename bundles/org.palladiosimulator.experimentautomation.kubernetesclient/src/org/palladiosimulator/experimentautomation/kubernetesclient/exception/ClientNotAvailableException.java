package org.palladiosimulator.experimentautomation.kubernetesclient.exception;

public class ClientNotAvailableException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -7707144989141937291L;

  public ClientNotAvailableException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public ClientNotAvailableException(String errorMessage) {
    super(errorMessage);
  }

}
