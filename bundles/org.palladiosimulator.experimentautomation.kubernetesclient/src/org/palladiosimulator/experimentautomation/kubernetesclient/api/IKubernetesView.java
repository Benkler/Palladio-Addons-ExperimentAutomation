package org.palladiosimulator.experimentautomation.kubernetesclient.api;


public interface IKubernetesView {

  /**
   * Send message to view to let the view decide how to display
   * 
   * @param message
   */
  void sendMessage(String message);

}
