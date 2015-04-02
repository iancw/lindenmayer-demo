/**
 * 
 */
package edu.gwu.graphics2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.gwu.graphics2.ConfigurableShading.ShadingMode;


/**
 * Lab1Display is the gui management class.  It provides text fields for
 * user entry of camera properties, and for selection of other model files.
 * 
 * It is invoked using the show method, and from there it gathers user input
 * and transfers control to the supplied Lab1Controller object when the Apply
 * button is clicked, resulting in updated camera properties, a new model transform,
 * and a canvas repaint.
 * 
 * @author ian
 *
 */
public class LabDisplay
{
	private final Logger logger = Logger.getLogger(LabDisplay.class.getName());
	
	private JTextField camera;
	private JTextField light;
	private JTextField reference;
	private JTextField up;
	private JTextField ambient;
	private JTextField diffuse;
	private JTextField specular;
	private JSpinner specularity;
	private JTextField h;
	private JTextField d;
	private JTextField f;
	private JComboBox combo;
	private SwingCanvas canvas;
	private Dimension dimension;
	private MouseController mouse;
	private LabController controller;
	private JCheckBox wireframe;
	private JCheckBox zbuffer;
	private JCheckBox fill;
	private JComboBox shader;
	private AtomicBoolean updating = new AtomicBoolean(false);
	private final String[] options = new String[]{"Clockwise", "Counter-Clockwise"};
	private final String[] shaders = new String[]{"Constant", "Gourard", "Phong"};
	private JLabel textureLabel;
	private JTextArea lSystem;
	private JSpinner recursions;
	private JTextField length;
	private JTextField angle;
	private BufferedImageTexture textureFile = null; 
	
	public void show(final LabController controller)
	{
		dimension = new Dimension(500, 500);
		mouse = new MouseController(controller);
		this.controller = controller;
		updating.set(false);
		
		canvas = new SwingCanvas(dimension);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		
		controller.renderModels(canvas);
		
		controller.addListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e) {
				if(!updating.get())
				{
					initFields((LabSettings)e.getSource());
					updateDisplay();
				}
			}
		});
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridy=0;
		infoPanel.add(new JLabel("Camera: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Light: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Reference Point: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Up Vector: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("h: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("d: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("f: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Vertex order: "), c);
		c.gridy++;
		wireframe = new JCheckBox("Wireframe");
		wireframe.setSelected(false);
		infoPanel.add(wireframe, c);
		c.gridy++;
		zbuffer = new JCheckBox("Display ZBuffer Algorithm");
		zbuffer.setSelected(false);
		infoPanel.add(zbuffer, c);
		c.gridy++;
		infoPanel.add(new JLabel("Ambient (ka): "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Diffuse (kd): "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Specular (ks): "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Specularity: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Shading:"), c);
		c.gridy++;
		infoPanel.add(new JLabel("Model: "), c);
		c.gridy++;
		textureLabel = new JLabel("Texture: ");
		infoPanel.add(textureLabel, c);
		c.gridy++;
		infoPanel.add(new JLabel("Recursions: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Length: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("Angle: "), c);
		c.gridy++;
		infoPanel.add(new JLabel("L System: "), c);
		
		c.gridx=1;
		c.gridy=0;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 1;
		camera = new JTextField();
		light = new JTextField();
		reference = new JTextField();
		up = new JTextField();
		d = new JTextField();
		h = new JTextField();
		f = new JTextField();
		ambient = new JTextField();
		specular = new JTextField();
		diffuse = new JTextField();
		specularity = new JSpinner();
		
		combo = new JComboBox(options);
		combo.setSelectedIndex(0);
		
		
		shader = new JComboBox(ShadingMode.values());
		lSystem = new JTextArea();
		recursions = new JSpinner();
		length = new JTextField();
		angle = new JTextField();
		
		infoPanel.add(camera, c);
		c.gridy++;
		infoPanel.add(light, c);
		c.gridy++;
		infoPanel.add(reference, c);
		c.gridy++;
		infoPanel.add(up, c);
		c.gridy++;
		infoPanel.add(h, c);
		c.gridy++;
		infoPanel.add(d, c);
		c.gridy++;
		infoPanel.add(f, c);
		c.gridy++;
		infoPanel.add(combo, c);
		c.gridy++;
		fill = new JCheckBox("Fill Polygons");
		fill.setSelected(true);
		infoPanel.add(fill, c);
		c.gridy+=2;
		infoPanel.add(ambient, c);
		c.gridy++;
		infoPanel.add(diffuse, c);
		c.gridy++;
		infoPanel.add(specular, c);
		c.gridy++;
		infoPanel.add(specularity, c);
		c.gridy++;
		infoPanel.add(shader, c);
		c.gridy++;
		JButton choose = new JButton("Choose Model...");
		infoPanel.add(choose, c);
		c.gridy++;
		JButton texture = new JButton("Choose Texture...");
		infoPanel.add(texture, c);
		c.gridy++;
		infoPanel.add(recursions, c);
		c.gridy++;
		infoPanel.add(length, c);
		c.gridy++;
		infoPanel.add(angle, c);
		c.gridy++;
		infoPanel.add(lSystem, c);
		c.gridy++;
		
		
		
		initFields(controller.getSettings());
		
		JButton apply = new JButton("Apply");
		infoPanel.add(apply, c);
		apply.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					controller.updateView(getSettings(), canvas.getSize());					
				}catch(Exception ex)
				{
					logger.warning("Invalid input, try again.");
					logger.warning(ex.getClass().getName()+": "+ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(canvas, BorderLayout.CENTER);
		panel.add(infoPanel, BorderLayout.EAST);
		final JFrame frame = new JFrame();
		frame.getRootPane().setDefaultButton(apply);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setSize(300, 300);
		frame.pack();
		

		choose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir")+"/models");
				chooser.setFileFilter(new FileNameExtensionFilter("Model data files (*.d)", "d"));
				chooser.setMultiSelectionEnabled(true);
				int res = chooser.showOpenDialog(frame);
				if(res == JFileChooser.APPROVE_OPTION)
				{
					File[] files = chooser.getSelectedFiles();
					String title = "";
					int i=0;
					for(File f : files)
					{
						title += f.getName();
						if(i < files.length -1)	
						{
							title+=",";
						}
						i++;
					}
					frame.setTitle(title);
					//controller.loadModels(files);
					controller.setLSystem(lSystem.getText());
					updateDisplay();
				}
			}
		});
		
		texture.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
				chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif"));
				chooser.setMultiSelectionEnabled(false);
				int res = chooser.showOpenDialog(frame);
				if(res == JFileChooser.APPROVE_OPTION)
				{
					File file = chooser.getSelectedFile();
					textureLabel.setText("Texture: "+file.getName());
					controller.loadTexture(file);
					textureFile = controller.getSettings().getTexture();
				}
			}
		});
		frame.setTitle(controller.getModelName());
		frame.setVisible(true);		
	}
	
	private synchronized void updateDisplay()
	{
		if(!updating.get())
		{
			updating.set(true);
			new Thread(new Runnable()
			{
				public void run()
				{
					if(combo.getSelectedIndex() == 0)
					{
						controller.setClockwise(true);
					}else
					{
						controller.setClockwise(false);
					}
					canvas.setWireframe(wireframe.isSelected());
					canvas.setPolygonFill(fill.isSelected());
					canvas.setDisplayZBuffer(zbuffer.isSelected());
					controller.renderModels(canvas);
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							canvas.repaint();
							updating.set(false);
						}
					});
				}
			}, "Rendering...").start();
			
		}
	}
	
	public LabSettings getSettings()
	{
		LabSettings settings = new LabSettings(
				Vector.parseVector(camera.getText()),
				Vector.parseVector(reference.getText()),
				Vector.parseVector(up.getText()),
				Float.parseFloat(d.getText()),
				Float.parseFloat(h.getText()), 				
				Float.parseFloat(f.getText()),
		new Light(Vector.parseVector(light.getText()), Color.white),
		Vector.parseVector(ambient.getText()),
		Vector.parseVector(diffuse.getText()),
		Vector.parseVector(specular.getText()),
		(Integer)specularity.getValue(),
		getShadingMode(),
		textureFile,
		lSystem.getText(),
		(Integer)recursions.getValue(),
		Double.parseDouble(length.getText()),
		Double.parseDouble(angle.getText())
		);
		return settings;
	}
	
	private ShadingMode getShadingMode()
	{
		return ShadingMode.valueOf(shader.getSelectedItem().toString());
	}
	
	private String formatVector(Vector v)
	{
		
		return String.format("%.02f, %.02f, %.02f", v.getX(), v.getY(), v.getZ());
	}
	
	private void initFields(LabSettings settings)
	{
		camera.setText(formatVector(settings.getCamera()));
		light.setText(formatVector(settings.getLight().getPosition()));
		reference.setText(formatVector(settings.getReference()));
		up.setText(formatVector(settings.getUp()));
		d.setText(""+settings.getD());
		h.setText(""+settings.getH());
		f.setText(""+settings.getF());
		ambient.setText(formatVector(settings.getKa()));
		diffuse.setText(formatVector(settings.getKd()));
		specular.setText(formatVector(settings.getKs()));
		specularity.setValue(settings.getSpecularity());
		shader.setSelectedItem(settings.getShadingModel());
		lSystem.setText(settings.getLSystemRules());
		recursions.setValue(settings.getRecursion());
		length.setText(""+settings.getTurtleLength());
		angle.setText(""+settings.getTurtleAngle());
	}
}
