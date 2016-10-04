/*
 * Copyright (c) 2016 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package ru.fors.sample.web.login;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.fors.sample.core.UserService;
import ru.fors.sample.core.dto.UserAuth;
import ru.fors.sample.core.security.SampleAuthenticationToken;
import ru.fors.sample.core.exception.BadUserNameOrPassword;
import ru.fors.sample.web.security.SampleSession;

/**
 * Author: Lebedev Aleksandr
 * Date: 24.02.16
 */
public class LoginPanel extends Panel {
    private static final long serialVersionUID = 109726690160923066L;

    private static final Logger logger = LoggerFactory.getLogger(LoginPanel.class);

    @SpringBean(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    private final UserAuth userInfo = new UserAuth();

    public LoginPanel(final String id) {
        super(id);
    }

    @SpringBean
    private UserService userService;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form<UserAuth> form = new StatelessForm<UserAuth>("signInForm", new CompoundPropertyModel<>(userInfo)) {
            private static final long serialVersionUID = 414702355957333785L;

            @Override
            protected void onSubmit() {

                Authentication request = new UsernamePasswordAuthenticationToken(userInfo.getUsername(), userInfo.getPassword());
                try {
                    Authentication result = authenticationManager.authenticate(request);
                    this.getSession().replaceSession();

                    if (result instanceof SampleAuthenticationToken) {
                        SampleSession.get().setUser(userService.getUserById(((SampleAuthenticationToken) result).getId()));
                    }
                    SecurityContextHolder.getContext().setAuthentication(result);
                    continueToOriginalDestination();

                    setResponsePage(getApplication().getHomePage());

                } catch (LockedException e) {
                    error(getString("account.locked"));

                } catch (BadUserNameOrPassword e) {
                    error(e.getMessage());

                } catch (AuthenticationException e) {
                    logger.error("AuthenticationException для пользователя {}. Ошибка: {}",
                            userInfo.getUsername(), e.getMessage());
                    error(e.getMessage());
                }
            }
        };

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(form));
        add(feedbackPanel.setOutputMarkupPlaceholderTag(true));

        form.add(new TextField<String>("username").setLabel(Model.of("Имя пользователя"))
                .setRequired(true)
                .setOutputMarkupId(true)
        );
        form.add(new PasswordTextField("password").setLabel(Model.of("Пароль"))
                .setRequired(true).setOutputMarkupId(true));
        add(form);
    }

}
