package email.service;

import com.prapps.aws.email.MimeMessageRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/email")
public class EmailController {
    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    @Get("/")
    public String hello() {
        return "hello from email controller";
    }

    @Post("/send")
    public String sendEmail(@Body MimeMessageRequest messageRequest) {
        LOG.trace("messageRequest: "+messageRequest);
        EmailServiceFunction function = new EmailServiceFunction();
        return function.apply(messageRequest);
    }
}
