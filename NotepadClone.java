import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class NotepadClone extends JFrame implements ActionListener {
    JTextArea textArea;
    JMenuBar menuBar;
    JMenu fileMenu, editMenu, helpMenu;
    JMenuItem newItem, openItem, saveItem, exitItem;
    JMenuItem cutItem, copyItem, pasteItem, selectAllItem, fontItem, aboutItem;

    public NotepadClone() {
        setTitle("Notepad Clone");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        menuBar = new JMenuBar();

        // File Menu
        fileMenu = new JMenu("File");
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit Menu
        editMenu = new JMenu("Edit");
        cutItem = new JMenuItem("Cut");
        copyItem = new JMenuItem("Copy");
        pasteItem = new JMenuItem("Paste");
        selectAllItem = new JMenuItem("Select All");
        fontItem = new JMenuItem("Font");

        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        selectAllItem.addActionListener(this);
        fontItem.addActionListener(this);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(selectAllItem);
        editMenu.add(fontItem);

        // Help Menu
        helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newItem) {
            textArea.setText("");
        } else if (e.getSource() == openItem) {
            openFile();
        } else if (e.getSource() == saveItem) {
            saveFile();
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        } else if (e.getSource() == cutItem) {
            textArea.cut();
        } else if (e.getSource() == copyItem) {
            textArea.copy();
        } else if (e.getSource() == pasteItem) {
            textArea.paste();
        } else if (e.getSource() == selectAllItem) {
            textArea.selectAll();
        } else if (e.getSource() == fontItem) {
            chooseFont();
        } else if (e.getSource() == aboutItem) {
            JOptionPane.showMessageDialog(this, "Notepad Clone\nVersion 1.0\nAuthor: Your Name");
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                textArea.setText("");
                String line;
                while ((line = br.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage());
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
                bw.write(textArea.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }

    private void chooseFont() {
        Font currentFont = textArea.getFont();
        Font selectedFont = JFontChooser.showDialog(this, "Choose Font", currentFont);
        if (selectedFont != null) {
            textArea.setFont(selectedFont);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NotepadClone notepad = new NotepadClone();
            notepad.setVisible(true);
        });
    }
}

// Custom Font Chooser Class
class JFontChooser extends JDialog {
    private Font selectedFont;

    public JFontChooser(Frame owner, Font currentFont) {
        super(owner, "Select Font", true);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JLabel label = new JLabel("Choose Font:");
        String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontList = new JComboBox<>(fontFamilies);
        fontList.setSelectedItem(currentFont.getFontName());

        JLabel styleLabel = new JLabel("Style:");
        String[] styles = {"Plain", "Bold", "Italic", "Bold Italic"};
        JComboBox<String> styleList = new JComboBox<>(styles);
        styleList.setSelectedItem(getStyleName(currentFont.getStyle()));

        JLabel sizeLabel = new JLabel("Size:");
        JComboBox<Integer> sizeList = new JComboBox<>();
        for (int i = 8; i <= 72; i++) {
            sizeList.addItem(i);
        }
        sizeList.setSelectedItem(currentFont.getSize());

        panel.add(label);
        panel.add(fontList);
        panel.add(styleLabel);
        panel.add(styleList);
        panel.add(sizeLabel);
        panel.add(sizeList);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            String fontName = (String) fontList.getSelectedItem();
            int style = getStyleValue((String) styleList.getSelectedItem());
            int size = (Integer) sizeList.getSelectedItem();
            selectedFont = new Font(fontName, style, size);
            setVisible(false);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            selectedFont = null;
            setVisible(false);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    private String getStyleName(int style) {
        switch (style) {
            case Font.BOLD:
                return "Bold";
            case Font.ITALIC:
                return "Italic";
            case Font.BOLD | Font.ITALIC:
                return "Bold Italic";
            default:
                return "Plain";
        }
    }

    private int getStyleValue(String style) {
        switch (style) {
            case "Bold":
                return Font.BOLD;
            case "Italic":
                return Font.ITALIC;
            case "Bold Italic":
                return Font.BOLD | Font.ITALIC;
            default:
                return Font.PLAIN;
        }
    }

    public static Font showDialog(Frame owner, String title, Font currentFont) {
        JFontChooser chooser = new JFontChooser(owner, currentFont);
        chooser.setVisible(true);
        return chooser.getSelectedFont();
    }
}