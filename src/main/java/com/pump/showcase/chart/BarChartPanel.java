package com.pump.showcase.chart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * A panel that displays a BarChartRenderer
 */
public class BarChartPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	class BarChartPanelUI extends BasicPanelUI {
		@Override
		public void paint(Graphics g, JComponent c) {
			super.paint(g, c);

			BarChartPanel p = (BarChartPanel) c;
			p.renderer.paint((Graphics2D) g, p.getSize());
		}
	}

	BarChartRenderer renderer;

	public BarChartPanel(BarChartRenderer renderer, Dimension preferredSize,
			Dimension minimumSize) {
		this.renderer = Objects.requireNonNull(renderer);
		setUI(new BarChartPanelUI());
		setMinimumSize(minimumSize);
		setPreferredSize(preferredSize);
		setOpaque(false);
	}
}
