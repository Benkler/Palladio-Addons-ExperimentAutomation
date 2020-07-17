package org.palladiosimulator.experimentautomation.kubernetesclient.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.*;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.IExperimentHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.experimentloader.ExperimentHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class KubernetesView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.palladiosimulator.experimentautomation.kubernetesclient.views.KubernetesView";

	@Inject
	IWorkbench workbench;

	private Text hostTextField;
	private Text pathToExperimentFileTextField;
	private Table simulationsTable;
	private Composite parent;
	private IExperimentHandler experimentHandler;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		experimentHandler = new ExperimentHandler();
		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(parent, "org.palladiosimulator.experimentautomation.kubernetesclient.viewer");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		parent.setLayout(gridLayout);

		makeIPAdressLabel();
		makeFileBrowser();
		makeSendButton();
		makeRefreshExperimentsButton();
		makeExperimentsTable();
	}

	private void makeExperimentsTable() {

		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.horizontalSpan = 4;
		Table table = new Table(parent, SWT.BORDER | SWT.MULTI
		        | SWT.FULL_SELECTION);
		table.setLayoutData(data);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn simulationNamecolumn = new TableColumn(table, SWT.None);
		simulationNamecolumn.setText("Simulation Name");
		simulationNamecolumn.setWidth(350);
		simulationNamecolumn.setAlignment(SWT.CENTER);
		
		TableColumn simulationStatusColumn = new TableColumn(table, SWT.None);
		simulationStatusColumn.setText("Simulation Status");
		simulationStatusColumn.setWidth(150);
		simulationStatusColumn.setAlignment(SWT.CENTER);
		
		TableColumn simulationCreationTimeColumn = new TableColumn(table, SWT.None);
		simulationCreationTimeColumn.setText("Creation Time");
		simulationCreationTimeColumn.setWidth(180);
		simulationCreationTimeColumn.setAlignment(SWT.CENTER);
		
		TableColumn simulationLogsColumn = new TableColumn(table, SWT.None);
		simulationLogsColumn.setText("Get Logs");
		simulationLogsColumn.setWidth(160);
		simulationLogsColumn.setAlignment(SWT.CENTER);
		
		TableColumn simulationDownloadColumn = new TableColumn(table, SWT.None);
		simulationDownloadColumn.setText("Download Results");
		simulationDownloadColumn.setWidth(200);
		simulationDownloadColumn.setAlignment(SWT.CENTER);
		
		
		
//
//		for (int i = 0; i < titles.length; i++) {
//			table.getColumn(i).pack();
//		}
		// table.setSize(table.computeSize(SWT.DEFAULT, 200));

		this.simulationsTable = table;

	}

	private void refreshExperimentsTable() {
		String kubernetesClientHost = hostTextField.getText();
		List<SimulationVO> simulations;
		try {
			simulations = experimentHandler.getExistingSimulation(kubernetesClientHost);
		} catch (ClientNotAvailableException | ExperimentException e) {
			// TODO Auto-generated catch block
			showMessage(e.getMessage());
			return;
		}
		Table table = this.simulationsTable;


		// Clear all rows
		table.removeAll();

		
		for (SimulationVO simulation : simulations) {
			TableItem row = new TableItem(table, SWT.NONE);
			
			
			TableEditor editor = new TableEditor(table);
			editor = new TableEditor(table);
			Text simulationNameText = new Text(table, SWT.NONE);
			simulationNameText.setText(simulation.getSimulationName());
			editor.grabHorizontal = true;
			editor.setEditor(simulationNameText, row, 0);

			editor = new TableEditor(table);
			Text simulationStatusText = new Text(table, SWT.NONE);
			simulationStatusText.setText(simulation.getSimulationStatus().getStatus());
			editor.grabHorizontal = true;
			editor.setEditor(simulationStatusText, row, 1);

			editor = new TableEditor(table);
			Text simulationTimestampText = new Text(table, SWT.NONE);
			simulationTimestampText.setText(simulation.getCreationTimeStamp());
			editor.grabHorizontal = true;
			editor.setEditor(simulationTimestampText, row, 2);
			
			editor = new TableEditor(table);
			Button downloadLogButton = makeDownloadButton(table, simulation);
			editor.grabHorizontal = true;
			editor.setEditor(downloadLogButton, row, 3);
			
			editor = new TableEditor(table);
			Button statusButton = new Button(table, SWT.PUSH);
			statusButton.setText("Status");
			editor.grabHorizontal = true;
			editor.setEditor(statusButton, row, 4);



		}

		

	}
	
	private Button makeDownloadButton(Table table, SimulationVO simulation) {
		Button downloadLogButton = new Button(table, SWT.PUSH);
		downloadLogButton.setText("Download Log");
		downloadLogButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog filesSave = new FileDialog(parent.getShell(), SWT.SAVE);
				filesSave.setFilterNames(new String[] {"TXT"});
				filesSave.setFilterExtensions(new String[] {".txt"});
				filesSave.setFileName(simulation.getSimulationName()+ "_log.txt");
				
				String fileName = filesSave.open();
				if(fileName != null) {
					File file = new File(fileName);
					try {
						file.createNewFile();
					} catch (IOException e1) {
						showMessage("Could not create File!");
					}
				}else {
					showMessage("Please specify a proper file location.");
				}
				
			}
		});
		return downloadLogButton;
	}

	private void makeRefreshExperimentsButton() {
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

	private void makeSendButton() {

		new Label(parent, SWT.NULL).setText("Send Experiment:");

		// Clicking the button will allow the user
		// to select a directory
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Send experiment data");

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		data.horizontalSpan = 3;
		button.setLayoutData(data);
		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				switch (event.type) {
				case SWT.Selection:
					String pathToExperimentFile = pathToExperimentFileTextField.getText();
					String kubernetesClientHost = hostTextField.getText();

					try {
						SimulationVO simulation = experimentHandler.sendExperimentData(pathToExperimentFile, kubernetesClientHost);
						refreshExperimentsTable();
						showMessage("Successfully started simulation with name: " + simulation.getSimulationName());
					} catch (ExperimentException | ClientNotAvailableException e) {
						showMessage(e.getMessage());
					}
					break;
				}

			}
		});
	}

	private void makeFileBrowser() {
		new Label(parent, SWT.NULL).setText("Path to experiment file:");
		// Create the text box extra wide to show long paths
		pathToExperimentFileTextField = new Text(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		pathToExperimentFileTextField.setLayoutData(data);

		// Clicking the button will allow the user
		// to select a directory
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				FileDialog dlg = new FileDialog(parent.getShell());

				// Change the title bar text
				dlg.setText("KubernetesClient");

				String[] approvedExtensions = { "*.experiments" };
				dlg.setFilterExtensions(approvedExtensions);

				String dir = dlg.open();
				if (dir != null) {

					pathToExperimentFileTextField.setText(dir);
				}
			}
		});
	}

	// TODO suggestions
	private void makeIPAdressLabel() {
		new Label(parent, SWT.NULL).setText("Kubernetes Client Host:");
		hostTextField = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 3;
		hostTextField.setLayoutData(gridData);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				KubernetesView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(parent);
		parent.setMenu(menu);

	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		//TODO pulldown menu needed?
	}

	private void fillContextMenu(IMenuManager manager) {
		//TODO add actions here like manager.add(action);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		//TODO toolbar needed?
	}

	private void makeActions() {
		//TODO actions for toolbar needed?
	}

	/*
	 * Opens Message Dialog
	 */
	private void showMessage(String message) {
		MessageDialog.openInformation(parent.getShell(), "Kubernetes View", message);
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}
}
