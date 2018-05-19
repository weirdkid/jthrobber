package com.pump.showcase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.pump.inspector.InspectorGridBagLayout;
import com.pump.inspector.InspectorLayout;
import com.pump.plaf.AquaThrobberUI;
import com.pump.plaf.ChasingArrowsThrobberUI;
import com.pump.plaf.LabelCellRenderer;
import com.pump.plaf.PulsingCirclesThrobberUI;
import com.pump.plaf.ThrobberUI;
import com.pump.swing.ColorWell;
import com.pump.swing.JThrobber;

public class ThrobberDemo extends JPanel implements ShowcaseDemo {
	private static final long serialVersionUID = 1L;

	JPanel controls = new JPanel();
	JComboBox<Class<? extends ThrobberUI>> typeComboBox = new JComboBox<>();
	JSlider sizeSlider = new JSlider(8, 100, 16);
	JSlider rateSlider = new JSlider(50, 200, 100);
	JThrobber throbber = new JThrobber();
	ColorWell color = new ColorWell(false, new Color(100, 0, 120));
	JCheckBox colorCheckBox = new JCheckBox("Custom Foreground:");

	public ThrobberDemo() {
		InspectorLayout layout = new InspectorGridBagLayout(controls);
		layout.addRow(new JLabel("Type:"), typeComboBox);
		layout.addRow(new JLabel("Size:"), sizeSlider);
		layout.addRow(new JLabel("Rate:"), rateSlider);
		layout.addRow(colorCheckBox, color);

		Dictionary<Integer, JLabel> dictionary = new Hashtable<Integer, JLabel>();
		dictionary.put(50, new JLabel("50%"));
		dictionary.put(100, new JLabel("100%"));
		dictionary.put(200, new JLabel("200%"));

		rateSlider.setPaintTicks(true);
		rateSlider.setPaintLabels(true);
		rateSlider.setMajorTickSpacing(50);
		rateSlider.setMinorTickSpacing(10);
		rateSlider.setLabelTable(dictionary);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		add(controls, c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		add(throbber, c);

		ChangeListener changeListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				refreshThrobber();
			}

		};
		rateSlider.addChangeListener(changeListener);
		sizeSlider.addChangeListener(changeListener);
		color.addChangeListener(changeListener);

		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshThrobber();
			}
		};

		colorCheckBox.addActionListener(actionListener);
		typeComboBox.addActionListener(actionListener);

		typeComboBox.addItem(AquaThrobberUI.class);
		typeComboBox.addItem(ChasingArrowsThrobberUI.class);
		typeComboBox.addItem(PulsingCirclesThrobberUI.class);

		typeComboBox
				.setRenderer(new LabelCellRenderer<Class<? extends ThrobberUI>>(
						typeComboBox, true) {

					@Override
					protected void formatLabel(Class<? extends ThrobberUI> value) {
						label.setText(value.getSimpleName());
					}

				});
	}

	private void refreshThrobber() {
		Class<?> c = (Class<?>) typeComboBox.getSelectedItem();
		ThrobberUI ui;
		try {
			ui = (ThrobberUI) c.newInstance();
			throbber.setUI(ui);
			int size = sizeSlider.getValue();
			throbber.setPreferredSize(new Dimension(size, size));
			throbber.getParent().revalidate();
			Float m = 100f / ((float) rateSlider.getValue());
			throbber.putClientProperty(ThrobberUI.PERIOD_MULTIPLIER_KEY, m);

			color.setEnabled(colorCheckBox.isSelected());
			if (colorCheckBox.isSelected()) {
				Color f = color.getColor();
				throbber.setForeground(f);
			} else {
				// installing a new UI will automatically change the foreground,
				// so if the checkbox is unselected we'll get the right color.
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getTitle() {
		return "Throbber Demo";
	}

	@Override
	public URL getHelpURL() {
		return ThrobberDemo.class.getResource("throbberDemo.html");
	}

	@Override
	public String[] getKeywords() {
		return new String[] { "throbber", "indicator", "feedback", "progress",
				"waiting" };
	}

	@Override
	public Class<?>[] getClasses() {
		return new Class[] { JThrobber.class, AquaThrobberUI.class,
				ChasingArrowsThrobberUI.class, PulsingCirclesThrobberUI.class };
	}

	@Override
	public boolean isSeparatorVisible() {
		return true;
	}

}