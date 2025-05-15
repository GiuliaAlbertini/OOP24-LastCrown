package it.unibo.oop.lastcrown.view;

import java.lang.reflect.Proxy;

import it.unibo.oop.lastcrown.view.characters.api.ReadOnlyAnimationPanel;

/**
 * A proxy that returns a protected version of the given Animation Panel.
 * It prevents all possible unpredicted external modification and it permits 
 * a read-only version of the panel.
 */
public final class AnimationPanelProxy {

    private AnimationPanelProxy() { }

    /**
     * Creates a safe version of the given Animation Panel that can be use externally.
     * @param originalPanel the original animation panel
     * @return a new ReadOnlyAnimationPanel
     */
    public static ReadOnlyAnimationPanel createSafePanel(final AnimationPanel originalPanel) {
        return (ReadOnlyAnimationPanel) Proxy.newProxyInstance(
            originalPanel.getClass().getClassLoader(),
            new Class<?>[]{ReadOnlyAnimationPanel.class}, 
            (proxy, method, args) -> {
                if (method.getName().startsWith("set") 
                    || method.getName().startsWith("add") 
                    || method.getName().startsWith("remove")) {
                    throw new UnsupportedOperationException("Modification not allowed");
                }
                return method.invoke(originalPanel, args);
            }
        );
    }
}

