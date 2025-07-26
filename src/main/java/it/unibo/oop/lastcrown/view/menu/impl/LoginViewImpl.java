package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.user.api.UsernameController;
import it.unibo.oop.lastcrown.controller.user.impl.UsernameControllerImpl;
import it.unibo.oop.lastcrown.view.menu.api.LoginView;
import it.unibo.oop.lastcrown.view.scenes_utilities.DocumentListenerImpl;

/**
 * View that represents the login scene of the application.
 * It provides a user interface for entering a username and validating it.
 */
public final class LoginViewImpl extends JFrame implements LoginView {
    private static final long serialVersionUID = 1L;
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final double RESIZE_FACTOR = 1.0;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int SCREEN_WIDTH = (int) (SCREEN_SIZE.getWidth() * RESIZE_FACTOR);
    private static final int SCREEN_HEIGHT = (int) (SCREEN_SIZE.getHeight() * RESIZE_FACTOR);
    private static final String FONT_NAME = "DialogInput";
    private static final double FIELD_HEIGHT_RATIO = 0.05;
    private static final double FIELD_WIDTH_RATIO = 0.15;
    private static final Color BG_COLOR = new Color(30, 60, 90);
    private static final Color TITLE_COLOR = new Color(255, 215, 0);
    private static final Color LABEL_COLOR = new Color(144, 238, 144);
    private static final Color BTN_BG_COLOR = new Color(255, 215, 0);
    private static final Color BTN_FG_COLOR = new Color(15, 35, 65);
    private static final Font TITLE_FONT = getResponsiveFont(Font.BOLD, 80);
    private static final Font SUBTITLE_FONT = getResponsiveFont(Font.BOLD, 40);
    private static final Font LABEL_FONT = getResponsiveFont(Font.BOLD, 30);
    private static final Font FIELD_FONT = getResponsiveFont(Font.BOLD, 28);
    private static final Font BTN_FONT = new Font("Monospaced", Font.BOLD, 26);
    private static final double[] V_SPACING = {0.2, 0.03, 0.05, 0.03, 0.2};

    private final transient UsernameController usernameController = new UsernameControllerImpl();
    private final transient MainController mainController;
    private final JPanel mainPanel = new JPanel();
    private final JTextField usernameField;
    private final JButton sendButton;

    /**
     * Initializes the view and add the controller to use.
     * 
     * @param mainController the MainController to use
     */
    private LoginViewImpl(final MainController mainController) {
        this.mainController = mainController;
        this.configureFrame();
        this.usernameField = createUsernameField();
        this.sendButton = createSendButton();
        this.arrangeComponents();
    }

    /**
     * Factory method to create a new LoginViewImpl.
     * 
     * @param mainController the MainController to use
     * @return the view created
     */
    public static LoginViewImpl create(final MainController mainController) {
        final LoginViewImpl view = new LoginViewImpl(mainController);
        view.setVisible(true);
        return view;
    }

    @Override
    public void close() {
        this.dispose();
    }

    private void configureFrame() {
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBackground(BG_COLOR);
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.setBackground(BG_COLOR);
        this.setContentPane(mainPanel);
    }

    private void arrangeComponents() {
        this.mainPanel.add(Box.createVerticalGlue());
        this.mainPanel.add(Box.createVerticalStrut(spacing(0)));
        this.mainPanel.add(createLabel("LAST CROWN", TITLE_FONT, TITLE_COLOR));
        this.mainPanel.add(Box.createVerticalStrut(spacing(1)));
        this.mainPanel.add(createLabel("Insert a username to continue", SUBTITLE_FONT, LABEL_COLOR));
        this.mainPanel.add(Box.createVerticalStrut(spacing(2)));
        this.mainPanel.add(createInputPanel());
        this.mainPanel.add(Box.createVerticalStrut(spacing(3)));
        this.mainPanel.add(this.sendButton);
        this.mainPanel.add(Box.createVerticalStrut(spacing(4)));
        this.mainPanel.add(Box.createVerticalGlue());
        this.setTransparency(mainPanel);
        this.usernameField.setOpaque(true);
        this.sendButton.setOpaque(true);
    }

    private JPanel createInputPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(BG_COLOR);
        panel.add(createLabel("Username:", LABEL_FONT, LABEL_COLOR));
        panel.add(usernameField);
        return panel;
    }

    private JButton createSendButton() {
        final JButton button = new JButton("SEND");
        button.setFont(BTN_FONT);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setBackground(BTN_BG_COLOR);
        button.setForeground(BTN_FG_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(BTN_BG_COLOR.brighter());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(BTN_BG_COLOR);
            }
        });

        button.addActionListener(sendActionListener());
        return button;
    }

    private JTextField createUsernameField() {
        final JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(
            (int) (SCREEN_WIDTH * FIELD_WIDTH_RATIO),
            (int) (SCREEN_HEIGHT * FIELD_HEIGHT_RATIO)));
        field.setFont(FIELD_FONT);
        field.getDocument().addDocumentListener(new DocumentListenerImpl(field));
        field.addActionListener(sendActionListener());
        return field;
    }

    private JLabel createLabel(final String text, final Font font, final Color fg) {
        final JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fg);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setFocusable(false);
        return label;
    }

    private ActionListener sendActionListener() {
        return e -> {
            final String user = usernameField.getText().trim().toLowerCase(Locale.ROOT);
            if (!usernameController.checkValidity(user)) {
                this.showError("Username field cannot be empty!");
            } else if (confirmUser(user)) {
                this.mainController.goOverLogin(user);
            }
            this.usernameField.setText("");
        };
    }

    private boolean confirmUser(final String user) {
        final boolean isNew = !usernameController.checkExistance(user);
        final int opt = JOptionPane.showConfirmDialog(
            this,
            String.format("This username is %s present in the records.%nDo you want to continue?",
                isNew ? "not " : "already "),
            "Confirm dialog",
            JOptionPane.YES_NO_OPTION);
        return opt == JOptionPane.YES_OPTION;
    }

    private void showError(final String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Input Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void setTransparency(final Container c) {
        if (c instanceof JComponent) {
            ((JComponent) c).setOpaque(false);
        }
        for (final Component comp : c.getComponents()) {
            if (comp instanceof Container) {
                setTransparency((Container) comp);
            }
        }
    }

    private int spacing(final int index) {
        return (int) (SCREEN_HEIGHT * V_SPACING[index]);
    }

    private static Font getResponsiveFont(final int style, final int size) {
        final double scale = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        return new Font(FONT_NAME, style, (int) (size * scale));
    }
}
