package org.palladiosimulator.experimentautomation.kubernetesclient.views;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.inject.Inject;
import org.apache.http.conn.util.InetAddressUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.ISimulationHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.IKubernetesView;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.InvalidClientURIException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;

/**
 * Represents the view of the Plug-In
 * 
 * @author Niko Benkler
 *
 */
public class KubernetesView extends ViewPart implements IKubernetesView {

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID =
      "org.palladiosimulator.experimentautomation.kubernetesclient.views.KubernetesView";

  @Inject
  IWorkbench workbench;

  private Text hostURITextField;
  private Text pathToExperimentFileTextField;
  private SimulationsTable simulationsTable;
  private Composite parent;
  private ISimulationHandler experimentHandler;



  @Override
  public void createPartControl(Composite parent) {
    this.parent = parent;
    experimentHandler = new SimulationHandler();
    // Create the help context id for the viewer's control
    workbench.getHelpSystem().setHelp(parent,
        "org.palladiosimulator.experimentautomation.kubernetesclient.viewer");


    // Basic Layout: Grid Layout with 4 cells
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 4;
    parent.setLayout(gridLayout);

    makeIPAdressGrid();
    makeFileBrowserGrid();
    makeSendExperimentGrid();
    makeRefreshSimulationTableGrid();
    makeExperimentsTableAsGrid();
  }

  /*
   * Creates label and text field for host adress
   */
  private void makeIPAdressGrid() {
    new Label(parent, SWT.NULL).setText("Kubernetes Client Host Adress:");
    hostURITextField = new Text(parent, SWT.SINGLE | SWT.BORDER);
    GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    gridData.horizontalSpan = 3;
    hostURITextField.setLayoutData(gridData);
  }

  /*
   * Creates Label, text field and button to search for experiments file
   */
  private void makeFileBrowserGrid() {
    new Label(parent, SWT.NULL).setText("Path to experiment file:");
    pathToExperimentFileTextField = new Text(parent, SWT.BORDER);
    GridData data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    pathToExperimentFileTextField.setLayoutData(data);

    Button button = new Button(parent, SWT.PUSH);
    button.setText("Browse...");
    button.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {

        FileDialog dlg = new FileDialog(parent.getShell());

        // Change the title bar text
        dlg.setText("KubernetesClient");

        String[] approvedExtensions = {"*.experiments"};
        dlg.setFilterExtensions(approvedExtensions);

        String dir = dlg.open();
        if (dir != null) {

          pathToExperimentFileTextField.setText(dir);
        }
      }
    });
  }

  /*
   * Creates Label and button to send experiments file
   */
  private void makeSendExperimentGrid() {

    new Label(parent, SWT.NULL).setText("Send Experiment:");

    Button button = new Button(parent, SWT.PUSH);
    button.setText("Send experiment data");

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
    data.horizontalSpan = 3;
    button.setLayoutData(data);
    button.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        validatePathToExperimentFile();
        switch (event.type) {
          case SWT.Selection:
            String pathToExperimentFile = pathToExperimentFileTextField.getText();

            try {
              SimulationVO simulation =
                  experimentHandler.sendExperimentData(pathToExperimentFile, getClientURI());
              refreshExperimentsTable();
              showMessage(
                  "Successfully started simulation with name: " + simulation.getSimulationName());
            } catch (ExperimentException | ClientNotAvailableException
                | InvalidClientURIException e) {
              showMessage(e.getMessage());
            }
            break;
        }

      }


    });
  }

  /*
   * Creates Label and Button to refresh simulation table
   */
  private void makeRefreshSimulationTableGrid() {
    new Label(parent, SWT.NULL).setText("Get current simulations:");

    // Clicking the button will allow the user
    // to select a directory
    Button button = new Button(parent, SWT.PUSH);
    button.setText("Refresh Experiments");

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
    data.horizontalSpan = 3;
    button.setLayoutData(data);
    button.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {

        switch (event.type) {
          case SWT.Selection:
            refreshExperimentsTable();
            break;
        }

      }
    });

  }

  /*
   * Create table that holds available simulations 
   */
  private void makeExperimentsTableAsGrid() {
  
    SimulationsTable table = new SimulationsTable(parent, this);
    GridData data = new GridData(GridData.FILL_VERTICAL);
    data.horizontalSpan = 4;
    table.setLayoutData(data);
    this.simulationsTable = table;
  
  }

  /*
   * Retrieve Client URI from text field.
   */
  private URI getClientURI() throws InvalidClientURIException {
    String clientHost = hostURITextField.getText();

    if (clientHost != null && clientHost.isBlank()) {
      throw new InvalidClientURIException("Client URI is invalid!");
    }

    try {
      return new URI(clientHost);
    } catch (URISyntaxException e) {
      throw new InvalidClientURIException(
          "Client URI is invalid!\n Please specify a valid URI like \n 'http://clientHost.com' or 'http://192.168.39.178:30164'");
    }


  }

  /*
   * Validate path: Not null and not empty
   */
  private boolean validatePathToExperimentFile() {
    String pathToExperimentFile = pathToExperimentFileTextField.getText();
    return pathToExperimentFile != null && !pathToExperimentFile.isBlank();
  }

  /*
   * Load current simulations status from client
   */
  private void refreshExperimentsTable() {
    List<SimulationVO> simulations;
    try {
      simulations = experimentHandler.getExistingSimulation(getClientURI());
      simulationsTable.updateTable(simulations, getClientURI(), experimentHandler);
    } catch (ClientNotAvailableException | ExperimentException | InvalidClientURIException e) {
      // TODO Auto-generated catch block
      showMessage(e.getMessage());
      return;
    }

  }



  /*
   * Opens Message Dialog
   */
  private void showMessage(String message) {
    MessageDialog.openInformation(parent.getShell(), "Kubernetes View", message);
  }

 
  @Override
  public void sendMessage(String message) {
    showMessage(message);
  }

  @Override
  public void setFocus() {
    parent.setFocus();
  }
}
