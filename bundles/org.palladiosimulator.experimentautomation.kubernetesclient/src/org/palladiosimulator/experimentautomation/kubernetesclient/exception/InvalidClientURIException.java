package org.palladiosimulator.experimentautomation.kubernetesclient.exception;

public class InvalidClientURIException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 7762942279010892986L;

  public InvalidClientURIException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public InvalidClientURIException(String errorMessage) {
    super(errorMessage);
  }

}
