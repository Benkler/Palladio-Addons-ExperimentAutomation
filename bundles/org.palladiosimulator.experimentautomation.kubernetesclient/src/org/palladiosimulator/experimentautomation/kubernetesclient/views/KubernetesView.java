package org.palladiosimulator.experimentautomation.kubernetesclient.views;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.palladiosimulator.experimentautomation.kubernetesclient.api.IExperimentHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ClientNotAvailableException;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.experimentloader.ExperimentHandler;
import org.palladiosimulator.experimentautomation.kubernetesclient.simulation.SimulationVO;

public class KubernetesView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.palladiosimulator.experimentautomation.kubernetesclient.views.KubernetesView";

	@Inject
	IWorkbench workbench;

	private Text hostTextField;
	private Text pathToExperimentFileTextField;
	private SortTable simulationsTable;
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

		SortTable table = new SortTable(parent, this);
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.horizontalSpan = 4;
		table.setLayoutData(data);
		this.simulationsTable = table;

	}

	private void refreshExperimentsTable() {
		String kubernetesClientHost = hostTextField.getText();
		List<SimulationVO> simulations;
		try {
			simulations = experimentHandler.getExistingSimulation(kubernetesClientHost);
			simulationsTable.updateTable(simulations, kubernetesClientHost, experimentHandler);
		} catch (ClientNotAvailableException | ExperimentException e) {
			// TODO Auto-generated catch block
			showMessage(e.getMessage());
			return;
		}

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
						SimulationVO simulation = experimentHandler.sendExperimentData(pathToExperimentFile,
								kubernetesClientHost);
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
		// TODO pulldown menu needed?
	}

	private void fillContextMenu(IMenuManager manager) {
		// TODO add actions here like manager.add(action);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		// TODO toolbar needed?
	}

	private void makeActions() {
		// TODO actions for toolbar needed?
	}

	/*
	 * Opens Message Dialog
	 */
	private void showMessage(String message) {
		MessageDialog.openInformation(parent.getShell(), "Kubernetes View", message);
	}

	public void sendMessage(String message) {
		showMessage(message);
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}
}
