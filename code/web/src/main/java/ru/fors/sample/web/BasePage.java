package ru.fors.sample.web;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import ru.fors.sample.core.security.AuthenticationService;
import ru.fors.sample.web.security.SampleSession;

@AuthorizeInstantiation(AuthenticationService.USER_ROLE)
public abstract class BasePage extends WebPage implements IAjaxIndicatorAware {
    private static final long serialVersionUID = 1L;

    public BasePage() {
        super();
    }

    public BasePage(PageReference callingPage) {

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public SampleSession getSession() {
        return SampleSession.get();
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return "ajax-indicator";
    }
}
