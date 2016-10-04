package ru.fors.sample.web;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.ExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.sample.web.login.SignInPage;
import ru.fors.sample.web.security.SampleSession;

import java.util.Locale;

//примечание: эта строка
//@Component("webApplication")
//при текущей конфигурации не нужна, т.к. описан уже бин:
//<bean id="wicketApplication" class="ru.fors.sample.web.WicketApplication"/>
//в main\resources\sample-web-config.xml
public class WicketApplication extends AuthenticatedWebApplication {
    private static final Logger logger = LoggerFactory.getLogger(WicketApplication.class);

    @Override
    public void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getMarkupSettings().setStripWicketTags(true);

        getApplicationSettings().setPageExpiredErrorPage(getSignInPageClass());
        getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_EXCEPTION_PAGE);

        mountPage("/login",getSignInPageClass());
        mountPage("/main", getHomePage());
    }


    @Override
    public Session newSession(org.apache.wicket.request.Request request,
                              org.apache.wicket.request.Response response) {
        SampleSession session = new SampleSession(request);
        session.setLocale(new Locale("ru", "RU"));
        return session;
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return SampleSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return SignInPage.class;
    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator locator = new ConverterLocator();
        return locator;
    }

    /*
        Узнать текущий режим приложения из любой точки приложения можно так:
        WicketApplication.get().getConfigurationType()
        Чтоб включить режим не-дебаг у себя локально, надо раскоментировать код ниже:
    */
    /*
        @Override
        public RuntimeConfigurationType getConfigurationType() {
            return RuntimeConfigurationType.DEPLOYMENT;
        }
    */
}
