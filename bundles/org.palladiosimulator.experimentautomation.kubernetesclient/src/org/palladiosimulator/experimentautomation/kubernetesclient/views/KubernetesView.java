package org.palladiosimulator.experimentautomation.kubernetesclient.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.*;
import org.palladiosimulator.experimentautomation.kubernetesclient.exception.ExperimentException;
import org.palladiosimulator.experimentautomation.kubernetesclient.experimentloader.ExperimentHandler;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

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

	// private TableViewer viewer;
	private Action action1;
	private Action action2;
	Text ipAdressTextField;
	Text pathToExperimentFileTextField;
	private Composite parent;
	private ExperimentHandler experimentLoader;

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
		experimentLoader = new ExperimentHandler();
		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(parent, "org.palladiosimulator.experimentautomation.kubernetesclient.viewer");
		// TODO hier war der viewer
		// getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		parent.setLayout(gridLayout);

		makeIPAdressLabel();
		makeFileBrowser();
		makeSendButton();
	}

	private void makeSendButton() {

		new Label(parent, SWT.NULL).setText("Send Experiment:");

		// Clicking the button will allow the user
		// to select a directory
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Send experiment data");

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 1;
		button.setLayoutData(data);
		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				switch (event.type) {
				case SWT.Selection:
					String pathToExperimentFile = pathToExperimentFileTextField.getText();

					try {
						experimentLoader.sendExperimentData(pathToExperimentFile);
					} catch (ExperimentException e) {
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

	private void makeIPAdressLabel() {
		new Label(parent, SWT.NULL).setText("Kubernetes Client IP:");
		ipAdressTextField = new Text(parent, SWT.SINGLE | SWT.BORDER);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 3;
		ipAdressTextField.setLayoutData(gridData);
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
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				new ShowDirectoryDialog(workbench.getDisplay()).run();
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

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
