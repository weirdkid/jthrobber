/**
 * This software is released as part of the Pumpernickel project.
 * 
 * All com.pump resources in the Pumpernickel project are distributed under the
 * MIT License:
 * https://raw.githubusercontent.com/mickleness/pumpernickel/master/License.txt
 * 
 * More information about the Pumpernickel project is available here:
 * https://mickleness.github.io/pumpernickel/
 */
package com.pump.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.pump.blog.ResourceSample;
import com.pump.icon.DarkenedIcon;
import com.pump.icon.PauseIcon;
import com.pump.icon.TriangleIcon;
import com.pump.plaf.GradientPanelUI;
import com.pump.plaf.PlafPaintUtils;

/**
 * A strip of GUI components that control an animation. The default size is very
 * narrow; it should be stretched horizontally to fill the width of its
 * container.
 *
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/resources/samples/AnimationController/sample.png"
 * alt="new&#160;com.pump.swing.animation.AnimationController()"> <!-- ========
 * END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = "new com.pump.swing.animation.AnimationController()")
public class AnimationController extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Format animation controls and add them to a container. This includes
	 * assigning the borders of all the components, assigning the PanelUI of the
	 * container, assigning the preferred size of the container, and making a
	 * spacebar on the slider trigger the toggle play button.
	 * 
	 * @param container
	 *            this container is emptied, assigned a GridBagLayout, and the
	 *            other arguments are added to this container.
	 * @param togglePlayButton
	 *            this button will be the left-most button. It is expected to
	 *            always be visible.
	 * @param buttons
	 *            an optional collection of buttons to display after the
	 *            togglePlayButton.
	 * @param slider
	 *            the slider that stretches to fill the remaining width.
	 */
	public static void format(JPanel container, final JButton togglePlayButton,
			JButton[] buttons, JSlider slider) {
		if (buttons == null)
			buttons = new JButton[] {};
		container.setUI(new GradientPanelUI(new Color(0xebebeb), Color.white));
		container.removeAll();
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		container.add(togglePlayButton, c);
		for (int a = 0; a < buttons.length; a++) {
			c.gridx++;
			container.add(buttons[a], c);
			buttons[a].setOpaque(false);
			buttons[a].setRolloverIcon(new DarkenedIcon(buttons[a], .5f));
			buttons[a].setPressedIcon(new DarkenedIcon(buttons[a], .75f));
			buttons[a].setBorder(new PartialLineBorder(Color.gray, new Insets(
					1, 0, 1, 1)));
		}
		c.weightx = 1;
		c.gridx++;
		c.fill = GridBagConstraints.BOTH;
		container.add(slider, c);

		togglePlayButton.setOpaque(false);
		togglePlayButton.setBorder(new LineBorder(Color.gray));
		slider.setOpaque(false);
		slider.setBorder(new PartialLineBorder(Color.gray, new Insets(1, 0, 1,
				1)));
		Dimension d = slider.getPreferredSize();
		d.width = 60;
		d.height = 25;
		slider.setPreferredSize((Dimension) d.clone());
		d.width = d.height;
		togglePlayButton.setPreferredSize(d);
		for (int a = 0; a < buttons.length; a++) {
			buttons[a].setPreferredSize(d);
		}

		InputMap inputMap = slider.getInputMap(JComponent.WHEN_FOCUSED);
		inputMap.put(KeyStroke.getKeyStroke(' '), "togglePlay");
		slider.getActionMap().put("togglePlay", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				togglePlayButton.doClick();
			}
		});

		togglePlayButton.setFocusPainted(false);
		setInsetFocusBorder(togglePlayButton);
		for (JButton button : buttons) {
			button.setFocusPainted(false);
			setInsetFocusBorder(button);
		}
		setInsetFocusBorder(slider);
	}

	private static void setInsetFocusBorder(JComponent jc) {
		Border oldBorder = jc.getBorder();
		Border newBorder = new FocusInsetBorder(jc);
		if (oldBorder == null) {
			jc.setBorder(oldBorder);
		} else {
			jc.setBorder(new CompoundBorder(oldBorder, newBorder));
		}
	}

	public static class FocusInsetBorder implements Border {
		FocusInsetBorder(JComponent jc) {
			jc.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					e.getComponent().repaint();
				}

				@Override
				public void focusLost(FocusEvent e) {
					e.getComponent().repaint();
				}

			});
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			if (c.isFocusOwner()) {
				g.clipRect(x, y, width, height);
				Rectangle r = new Rectangle(x - 1, y - 1, width + 1, height + 1);
				PlafPaintUtils.paintFocus((Graphics2D) g, r, 2);
			}
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(2, 2, 2, 2);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

	}

	static Icon playIcon = new TriangleIcon(SwingConstants.EAST, 12, 12);
	static Icon pauseIcon = new PauseIcon(12, 12);

	JButton playButton = new JButton();
	private static int SLIDER_MAXIMUM = 1000;
	JSlider slider = new JSlider(0, SLIDER_MAXIMUM);

	/**
	 * A client property that maps to a Boolean indicating whether this
	 * controller should loop.
	 */
	public static String LOOP_PROPERTY = AnimationController.class.getName()
			+ ".loop";
	/**
	 * A client property that maps to a Number indicating the current time (in
	 * seconds).
	 */
	public static String TIME_PROPERTY = AnimationController.class.getName()
			+ ".time";
	/**
	 * A client property that maps to a Number indicating the current duration
	 * (in seconds).
	 */
	public static String DURATION_PROPERTY = AnimationController.class
			.getName() + ".duration";
	/**
	 * A client property that maps to a Boolean indicating whether this
	 * controller is playing.
	 */
	public static String PLAYING_PROPERTY = AnimationController.class.getName()
			+ ".playing";

	ActionListener buttonListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (playButton.getIcon() == playIcon) {
				play();
			} else {
				pause();
			}
		}
	};

	static Timer timer = new Timer(1000 / 25, null);
	static {
		timer.start();
	}
	int adjustingSlider = 0;
	ChangeListener sliderListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			if (adjustingSlider > 0)
				return;
			float f = slider.getValue();
			f = f / (SLIDER_MAXIMUM);
			f = f * getDuration();
			setTime(f);
		}
	};

	public AnimationController() {
		this(new JButton[] {});
	}

	public AnimationController(JButton[] buttons) {
		super(new GridBagLayout());
		format(this, playButton, buttons, slider);

		setIcon(playButton, playIcon);
		playButton.addActionListener(buttonListener);
		slider.setValue(0);
		setTime(0);
		slider.addChangeListener(sliderListener);

		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (DURATION_PROPERTY.equals(evt.getPropertyName())) {
					updateSlider();
				} else if (TIME_PROPERTY.equals(evt.getPropertyName())) {
					if (isPlaying()) {
						lastStartTime = System.currentTimeMillis()
								- (long) (getTime() * 1000);
					}
					updateSlider();
				} else if (PLAYING_PROPERTY.equals(evt.getPropertyName())) {
					if (isPlaying()) {
						lastStartTime = System.currentTimeMillis()
								- (long) (getTime() * 1000);
						timer.addActionListener(actionListener);
						setIcon(playButton, pauseIcon);
					} else {
						timer.removeActionListener(actionListener);
						setIcon(playButton, playIcon);
					}
				} else if ("enabled".equals(evt.getPropertyName())) {
					playButton.setEnabled(isEnabled());
					slider.setEnabled(isEnabled());
				}
			}
		});
	}

	// TODO: implement ShapeIcon and set up animated changes.
	void setIcon(AbstractButton button, Icon icon) {
		button.setIcon(icon);
		button.setRolloverIcon(new DarkenedIcon(button, .5f));
		button.setPressedIcon(new DarkenedIcon(button, .75f));
	}

	protected void updateSlider() {
		float percent = getTime() / getDuration();
		int v = (int) (percent * SLIDER_MAXIMUM);
		if (v > SLIDER_MAXIMUM)
			v = SLIDER_MAXIMUM;
		adjustingSlider++;
		slider.setValue(v);
		adjustingSlider--;
	}

	private transient long lastStartTime = 0;
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (isPlaying() == false)
				return;

			float duration = getDuration();
			if (duration == 0)
				throw new RuntimeException(
						"Can't play an animation with a duration of 0 s.");
			long period = (long) (1000 * duration);
			long t = System.currentTimeMillis() - lastStartTime;
			if (isLooping()) {
				long k = t % period;
				float f = k;
				f = f / 1000f;
				setTime(f);
			} else {
				if (t < period) {
					float f = t;
					f = f / 1000f;
					setTime(f);
				} else {
					setTime(duration);
					pause();
				}
			}
		}
	};

	/** Returns the duration (in seconds) */
	public float getDuration() {
		Number n = (Number) getClientProperty(DURATION_PROPERTY);
		if (n == null)
			n = 1;
		return n.floatValue();
	}

	public void play() {
		if (isPlaying())
			return;

		if (Math.abs(getTime() - getDuration()) < .001) {
			setTime(0);
		}

		putClientProperty(PLAYING_PROPERTY, true);
	}

	public boolean isPlaying() {
		Boolean b = (Boolean) getClientProperty(PLAYING_PROPERTY);
		if (b == null)
			return false;
		return b;
	}

	public void pause() {
		putClientProperty(PLAYING_PROPERTY, false);
	}

	/** Return the playback slider. */
	public JSlider getSlider() {
		return slider;
	}

	/** Return the play/pause button. */
	public JButton getPlayButton() {
		return playButton;
	}

	public boolean isLooping() {
		Boolean b = (Boolean) getClientProperty(LOOP_PROPERTY);
		if (b == null)
			return false;
		return b;
	}

	public void setLooping(boolean b) {
		putClientProperty(LOOP_PROPERTY, b);
	}

	public void setDuration(float f) {
		putClientProperty(DURATION_PROPERTY, f);
	}

	public float getTime() {
		Number n = (Number) getClientProperty(TIME_PROPERTY);
		if (n == null)
			n = 0;
		return n.floatValue();
	}

	public void setTime(float f) {
		putClientProperty(TIME_PROPERTY, f);
	}
}