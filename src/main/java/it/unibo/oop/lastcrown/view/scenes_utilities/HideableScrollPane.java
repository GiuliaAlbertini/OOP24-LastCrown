package it.unibo.oop.lastcrown.view.scenes_utilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.JComponent;

/**
 * A JScrollPane that hides its vertical scrollbar by default and only shows it
 * when the user moves the mouse. After a timeout it hides it again.
 */
public final class HideableScrollPane extends JScrollPane {
    private static final long serialVersionUID = 1L;
    private static final int HIDE_DELAY_MS = 3000;
    private final Timer hideTimer;

    /**
     * Creates a new HideableScrollPane wrapping the given view component.
     * The vertical scrollbar is hidden by default and only appears while the user
     * moves the mouse over the viewport.
     *
     * @param view the component to put inside this scroll pane
     */
    public HideableScrollPane(final JComponent view) {
        super(view);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.hideTimer = new Timer(HIDE_DELAY_MS, e -> {
            setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        });
        hideTimer.setRepeats(false);

        getViewport().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
                hideTimer.restart();
            }
        });

        getVerticalScrollBar().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                hideTimer.stop();
            }
            @Override
            public void mouseReleased(final MouseEvent e) {
                hideTimer.restart();
            }
        });
    }
}
