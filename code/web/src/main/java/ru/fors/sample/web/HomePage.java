package ru.fors.sample.web;

import org.apache.wicket.markup.html.basic.Label;
import ru.fors.sample.web.security.SampleSession;

/**
 * @author Mikhail Kokorin
 *         date: 04.10.2016
 *         time: 16:20
 */
public class HomePage extends BasePage {

    public HomePage() {
        super();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("text", String.format("Добро пожаловать, %s", SampleSession.get().getUser().getName())));
    }

}
