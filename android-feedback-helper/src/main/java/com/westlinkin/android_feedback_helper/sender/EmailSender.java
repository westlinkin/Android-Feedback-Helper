/*
 * Copyright 2015 Wesley Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.westlinkin.android_feedback_helper.sender;

import android.content.Context;

import com.westlinkin.android_feedback_helper.R;
import com.westlinkin.android_feedback_helper.utils.MailUntils;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Wesley Lin on 3/23/15.
 */
public class EmailSender extends Authenticator {
    private static final String MAIL_HOST = "smtp.gmail.com";

    private Session session;
    private Context context;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public EmailSender(Context context) {
        this.context = context;
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", MAIL_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(
                MailUntils.emailNameProcessor(context.getString(R.string.afh_email_name)),
                MailUntils.emailPwdProcessor(context.getString(R.string.afh_email_pwd))
        );
    }

    /**
     * where feedback email is really sent
     * @param appName your app_name
     * @param body the feedback content
     * @param recipients where the feedback email is sent to, could be multiple addresses, split by comma
     * @throws Exception
     */
    public void send(String appName, String body, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setFrom(new InternetAddress(MailUntils.emailNameProcessor(context.getString(R.string.afh_email_name))));
            message.setSubject(MailUntils.getMailSubject(appName));
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
