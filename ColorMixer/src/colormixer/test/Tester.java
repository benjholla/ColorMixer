package colormixer.test;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import colormixer.KMColor;

public class Tester {

	protected Shell shell;
	
	private boolean aSet = false;
	private boolean bSet = false;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Tester window = new Tester();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 378);
		shell.setText("Tester");
		shell.setLayout(new GridLayout(1, false));
		
		final Label colorA = new Label(shell, SWT.NONE);
		colorA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		colorA.setText("");
		
		final Composite colorAColorComposite = new Composite(shell, SWT.NONE | SWT.BORDER);
		colorAColorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label plus = new Label(shell, SWT.NONE);
		plus.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		plus.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		plus.setText("+");
		
		final Label colorB = new Label(shell, SWT.NONE);
		colorB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		colorB.setText("");
		
		final Composite colorBColorComposite = new Composite(shell, SWT.NONE | SWT.BORDER);
		colorBColorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label equals = new Label(shell, SWT.NONE);
		equals.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		equals.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		equals.setText("=");
		
		final Label resultColor = new Label(shell, SWT.NONE);
		resultColor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		resultColor.setText("");
		
		final Composite resultColorComposite = new Composite(shell, SWT.NONE | SWT.BORDER);
		resultColorComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		colorAColorComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ColorDialog colorDialog = new ColorDialog(shell);
				colorDialog.setRGB(new RGB(128, 128, 128));
				RGB rgb = colorDialog.open();
				if (rgb != null) {
					Color color = new Color(Display.getCurrent(), rgb.red, rgb.green, rgb.blue);
					if (color != null) {
						colorAColorComposite.setBackground(color);
						colorA.setText("R: " + color.getRed() + ", G: " + color.getGreen() + ", B: " + color.getBlue());
						aSet = true;
					}
					if(aSet && bSet){
						setResultColor(colorAColorComposite.getBackground(), colorBColorComposite.getBackground(), resultColorComposite, resultColor);
					}
				}
			}
		});
		
		colorBColorComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ColorDialog colorDialog = new ColorDialog(shell);
				colorDialog.setRGB(new RGB(128, 128, 128));
				RGB rgb = colorDialog.open();
				if (rgb != null) {
					Color color = new Color(Display.getCurrent(), rgb.red, rgb.green, rgb.blue);
					if (color != null) {
						colorBColorComposite.setBackground(color);
						colorB.setText("R: " + color.getRed() + ", G: " + color.getGreen() + ", B: " + color.getBlue());
						bSet = true;
					}
					if(aSet && bSet){
						setResultColor(colorAColorComposite.getBackground(), colorBColorComposite.getBackground(), resultColorComposite, resultColor);
					}
				}
			}
		});
	}
	
	private void setResultColor(Color colorA, Color colorB, Composite resultColorComposite, Label resultColor){
		KMColor mix = new KMColor(new java.awt.Color(colorA.getRed(), colorA.getGreen(), colorA.getBlue()));
		mix.mix(new java.awt.Color(colorB.getRed(), colorB.getGreen(), colorB.getBlue()));
		java.awt.Color result = mix.getColor();
		resultColorComposite.setBackground(new Color(Display.getCurrent(), result.getRed(), result.getGreen(), result.getBlue()));
		resultColor.setText("R: " + result.getRed() + ", G: " + result.getGreen() + ", B: " + result.getBlue());
	}
}
