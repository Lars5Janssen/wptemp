/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

import wpcg.base.misc.AssetPath;
import wpcg.base.misc.Logger;
import wpcg.ui.base.ParameterEditor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Grammar (parameter) editor for an L-System.
 */
public class GrammarEditor<T> extends ParameterEditor {

  public GrammarEditor(GrammarParameters<T> params, String dir) {
    super(params);

    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(350, 600));

    // This text field contains the grammar as text.
    String grammarText = params.getGrammarText();
    JTextArea grammarArea = new JTextArea(grammarText);
    JScrollPane scrollPane = new JScrollPane(grammarArea);
    add(scrollPane, BorderLayout.CENTER);

    // File selection
    JComboBox<String> comboBoxFilename = new JComboBox<>();
    List<String> grammarFilenames = AssetPath.getInstance().getFilesInDir(dir, "grammar");
    for (String filename : grammarFilenames) {
      comboBoxFilename.addItem(filename);
    }
    comboBoxFilename.addActionListener(e -> {
      try {
        params.readGrammarFromFile((String) comboBoxFilename.getSelectedItem());
      } catch (GrammarException ex) {
        Logger.getInstance().error("Grammar error: " + ex.getMessage());
      }
      grammarArea.setText(params.getGrammarText());
    });
    add(comboBoxFilename, BorderLayout.NORTH);

    // Parse-button
    JButton button = new JButton("Parse + Regenerate");
    button.addActionListener(e -> {
      try {
        params.readGrammarFromString(grammarArea.getText());
        statusBarMessage("Grammar successfully parsed and applied.");
      } catch (GrammarException ex) {
        Logger.getInstance().error("Grammar error: " + ex.getMessage());
        statusBarMessage(ex.getMessage());
      }
    });
    add(button, BorderLayout.SOUTH);
  }
}
