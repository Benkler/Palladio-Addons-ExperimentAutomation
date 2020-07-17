package org.palladiosimulator.experimentautomation.kubernetesclient.views;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.ISimulationHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.IKubernetesView;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationStatusCode;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;

/**
 * Wrapper class for Table that holds SimulationVO objects. </br>
 * Table has a header an rows where each row represents a SimulationVO instance.
 * 
 * @author Niko Benkler
 *
 */
public class SimulationsTable {
  private List<TableRow> rows = new ArrayList<>();

  private Table table;
  private Composite parent;
  private TableColumn simulationNameColumn;
  private TableColumn simulationStatusColumn;
  private TableColumn simulationCreationTimeColumn;
  private TableColumn simulationLogsColumn;
  private TableColumn simulationDownloadColumn;
  private SimpleDateFormat simulationCreationTimeFormat =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private SimpleDateFormat simulationTableFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private URI clientURI;
  private ISimulationHandler experimentHandler;

  private IKubernetesView view;

  public void setLayoutData(Object layoutData) {
    table.setLayoutData(layoutData);
  }

  public SimulationsTable(Composite parent, IKubernetesView view) {
    this.parent = parent;
    this.view = view;

    table = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
    table.setLinesVisible(true);
    table.setHeaderVisible(true);

    simulationNameColumn = new TableColumn(table, SWT.None);
    simulationNameColumn.setText("Simulation Name");
    simulationNameColumn.setWidth(350);
    simulationNameColumn.setAlignment(SWT.CENTER);

    simulationStatusColumn = new TableColumn(table, SWT.None);
    simulationStatusColumn.setText("Simulation Status");
    simulationStatusColumn.setWidth(150);
    simulationStatusColumn.setAlignment(SWT.CENTER);

    simulationCreationTimeColumn = new TableColumn(table, SWT.None);
    simulationCreationTimeColumn.setText("Creation Time");
    simulationCreationTimeColumn.setWidth(180);
    simulationCreationTimeColumn.setAlignment(SWT.CENTER);

    simulationLogsColumn = new TableColumn(table, SWT.None);
    simulationLogsColumn.setText("Get Logs");
    simulationLogsColumn.setWidth(160);
    simulationLogsColumn.setAlignment(SWT.CENTER);

    simulationDownloadColumn = new TableColumn(table, SWT.None);
    simulationDownloadColumn.setText("Download Results");
    simulationDownloadColumn.setWidth(200);
    simulationDownloadColumn.setAlignment(SWT.CENTER);

    addColumnSelectionListener(simulationCreationTimeColumn);
    addColumnSelectionListener(simulationNameColumn);

  }

  private void addColumnSelectionListener(TableColumn column) {
    column.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        tableColumnClicked((TableColumn) e.widget);
      }
    });
  }

  private void tableColumnClicked(TableColumn column) {
    Table table = column.getParent();

    // set sortColumn and direction
    if (column.equals(table.getSortColumn())) {
      table.setSortDirection(table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP);
    } else {
      table.setSortColumn(column);
      table.setSortDirection(SWT.UP);
    }

    sortRowsAccordingToCurrentSortDirection();
    updateTable();
  }

  private void sortRowsAccordingToCurrentSortDirection() {
    TableColumn column = table.getSortColumn();
    int sortDirection = table.getSortDirection();

    // Default: sort per date descending
    if (column == null || sortDirection == SWT.NONE) {
      Collections.sort(rows, BY_DATE);
      Collections.reverse(rows);
    }

    if (sortDirection == SWT.UP && column == simulationNameColumn) {
      Collections.sort(rows, BY_NAME);
    } else if (sortDirection == SWT.UP && column == simulationCreationTimeColumn) {
      Collections.sort(rows, BY_DATE);
    } else if (sortDirection == SWT.DOWN && column == simulationNameColumn) {
      Collections.sort(rows, BY_NAME);
      Collections.reverse(rows);
    } else if (sortDirection == SWT.DOWN && column == simulationCreationTimeColumn) {
      Collections.sort(rows, BY_DATE);
      Collections.reverse(rows);
    }

  }

  public void updateTable(List<SimulationVO> simulations, URI clientURI,
      ISimulationHandler experimentHandler) throws ExperimentException {
    this.clientURI = clientURI;
    this.experimentHandler = experimentHandler;

    rows.clear();
    for (SimulationVO simulation : simulations) {
      rows.add(new TableRow(simulation));
    }
    sortRowsAccordingToCurrentSortDirection();
    updateTable();
  }

  private void updateTable() {

    table.removeAll();
    for (TableRow row : rows) {
      TableItem item = new TableItem(table, SWT.NONE);

      TableEditor editor = new TableEditor(table);
      editor = new TableEditor(table);
      Text simulationNameText = new Text(table, SWT.NONE);
      simulationNameText.setText(row.simulationName);
      editor.grabHorizontal = true;
      editor.setEditor(simulationNameText, item, 0);

      editor = new TableEditor(table);
      Text simulationStatusText = new Text(table, SWT.NONE);
      simulationStatusText.setText(row.simulationStatus);
      editor.grabHorizontal = true;
      editor.setEditor(simulationStatusText, item, 1);

      editor = new TableEditor(table);
      Text simulationTimestampText = new Text(table, SWT.NONE);
      simulationTimestampText.setText(simulationTableFormat.format(row.simulationCreationDate));
      editor.grabHorizontal = true;
      editor.setEditor(simulationTimestampText, item, 2);

      editor = new TableEditor(table);
      editor.grabHorizontal = true;
      editor.setEditor(row.downloadLogButton, item, 3);

      if (row.simulationStatusCode == SimulationStatusCode.SUCCEEDED) {
        editor = new TableEditor(table);
        editor.grabHorizontal = true;
        editor.setEditor(row.downloadResultsButton, item, 4);
      } else {
        editor = new TableEditor(table);
        editor.grabHorizontal = true;
        // Make empty label
        editor.setEditor(new Label(table, SWT.NULL), item, 4);
      }
    }
  }

  public final Comparator<TableRow> BY_NAME = new Comparator<TableRow>() {
    @Override
    public int compare(TableRow o1, TableRow o2) {
      return o1.simulationName.compareTo(o2.simulationName);
    }
  };

  public final Comparator<TableRow> BY_DATE = new Comparator<TableRow>() {
    @Override
    public int compare(TableRow o1, TableRow o2) {
      return o1.simulationCreationDate.compareTo(o2.simulationCreationDate);
    }
  };

  private class TableRow {
    private String simulationName;
    private String simulationStatus;
    private Date simulationCreationDate;

    private Button downloadLogButton;
    private Button downloadResultsButton;

    private SimulationStatusCode simulationStatusCode;

    public TableRow(SimulationVO simulation) throws ExperimentException {
      this.simulationName = simulation.getSimulationName();
      this.simulationStatus = simulation.getSimulationStatus().getStatus();
      this.simulationCreationDate =
          parserSimulationCreationTimeStamp(simulation.getCreationTimeStamp());
      this.simulationStatusCode = simulation.getSimulationStatus();
      this.downloadLogButton = makeDownloadLogButton(simulation);
      this.downloadResultsButton = makeDownloadResultFilesButton(simulation);
    }

    private Date parserSimulationCreationTimeStamp(String timeStamp) throws ExperimentException {
      try {
        return simulationCreationDate = simulationCreationTimeFormat.parse(timeStamp);
      } catch (ParseException e) {
        System.out.println(e.getMessage());
        // TODO logging
        throw new ExperimentException("Cannot format simulation creation time!");
      }
    }

  }

  private Button makeDownloadResultFilesButton(SimulationVO simulation) {

    Button resultButton = new Button(table, SWT.PUSH);
    resultButton.setText("Download Results");

    resultButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {

        byte[] zippedExperimentResults;

        try {
          zippedExperimentResults = experimentHandler
              .getZippedSimulationResultsAsByteArray(simulation.getSimulationName(), clientURI);
        } catch (ClientNotAvailableException | ExperimentException e1) {
          // TODO logging
          view.sendMessage(e1.getLocalizedMessage());
          return;
        }

        FileDialog filesSave = new FileDialog(parent.getShell(), SWT.SAVE);
        filesSave.setFilterNames(new String[] {"ZIP", "All Files (*.*)"});
        filesSave.setFilterExtensions(new String[] {"*.zip", "*.*"});
        filesSave.setFileName(simulation.getSimulationName() + "_results.zip");

        String fileName = filesSave.open();
        if (fileName != null) {
          Path path = Paths.get(fileName);
          try {

            // ZipOutputStream zos = new
            Files.write(path, zippedExperimentResults);
          } catch (IOException e2) {
            // TODO Auto-generated catch block
            view.sendMessage("Could not create File!");
          }

        } else {
          view.sendMessage("Please specify a proper file location.");
        }

      }
    });

    return resultButton;
  }

  private Button makeDownloadLogButton(SimulationVO simulation) {

    Button downloadLogButton = new Button(table, SWT.PUSH);
    downloadLogButton.setText("Download Log");
    downloadLogButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {

        String log;
        try {
          log = experimentHandler.getSimulationLog(simulation.getSimulationName(), clientURI);
        } catch (ClientNotAvailableException | ExperimentException e1) {
          view.sendMessage(e1.getMessage());
          return;

        }

        FileDialog filesSave = new FileDialog(parent.getShell(), SWT.SAVE);
        filesSave.setFilterNames(new String[] {"TXT", "All Files (*.*)"});
        filesSave.setFilterExtensions(new String[] {"*.txt", "*.*"});
        filesSave.setFileName(simulation.getSimulationName() + "_log.txt");

        String fileName = filesSave.open();
        if (fileName != null) {
          Path path = Paths.get(fileName);
          try {
            Files.write(path, log.getBytes());
          } catch (IOException e2) {
            // TODO Auto-generated catch block
            view.sendMessage("Could not create File!");
          }

        } else {
          view.sendMessage("Please specify a proper file location.");
        }

      }
    });
    return downloadLogButton;
  }

}
