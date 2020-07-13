package org.palladiosimulator.experimentautomation.kubernetesclient.views;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class BrowseForDirectory {
	Composite parent;
	
	public BrowseForDirectory(Composite parent) {
		this.parent = parent;
	}
	
	public void makeFileBrowser() {
		
		Label lblPleaseEnterA = new Label(parent, SWT.NONE);
		lblPleaseEnterA.setText("Please enter a value:");

		Text text = new Text(parent, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text.horizontalIndent = 8;
		text.setLayoutData(gd_text);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);

		// create the decoration for the text component
		// using an predefined image
		final ControlDecoration deco = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
		.getImage();
		// set description and image
		deco.setDescriptionText("Use CTRL + SPACE to see possible values");
		deco.setImage(image);
		// always show decoration
		deco.setShowOnlyOnFocus(false);

		// hide the decoration if the text widget has content
		text.addModifyListener(e -> {
		    Text source = (Text) e.getSource();
		    if (!source.getText().isEmpty()) {
		        deco.hide();
		    } else {
		        deco.show();
		    }
		});

		// Define field assists for the text widget
		// use "." and " " activate the content proposals
		char[] autoActivationCharacters = new char[] { '.', ' ' };
		KeyStroke keyStroke;
		try {
		    keyStroke = KeyStroke.getInstance("Ctrl+Space");
		    new ContentProposalAdapter(text, new TextContentAdapter(),
		    new SimpleContentProposalProvider(new String[] { "ProposalOne", "ProposalTwo", "ProposalThree" }),
		    keyStroke, autoActivationCharacters);
		} catch (ParseException e1) {
		    e1.printStackTrace();
		}
	}
	
	

}
