/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.ui.base;

import wpcg.base.model.Model;
import wpcg.base.misc.Observable;
import wpcg.base.misc.Observer;
import wpcg.ui.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Base viewer class for the viewers of the abstract model representation.
 */
public abstract class ModelViewer extends JPanel implements Observer {

  /**
   * Model object.
   */
  protected Model model;

  public ModelViewer(Model model) {
    this.model = model;
    model.addObserver(this);
  }

  /**
   * Override this method to get informed if the model changed.
   */
  @Override
  public void update(Observable sender, String descr, Object payload) {
  }

  public Model getModel() {
    return model;
  }

  /**
   * Add a panel for the settings.
   */
  public void setSettingsPanel(Component settingsPanel) {
    if (settingsPanel != null) {
      SwingUtilities.invokeLater(() -> {
        add(settingsPanel, BorderLayout.EAST);
      });
    }
  }

  /**
   * (Optionally) Provide a list of status bar items.
   */
  public List<StatusBar.StatusBarItem> getStatusBarItems() {
    return new ArrayList<>();
  }
}
