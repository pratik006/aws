package email.service;

import com.prapps.aws.email.MessageRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller("/email")
public class EmailController {

    @Get("/")
    public String hello() {
        return "hello from email controller";
    }

    @Post("/send")
    public String sendEmail(@Body MessageRequest messageRequest) {
        EmailServiceFunction function = new EmailServiceFunction();
        return function.apply(messageRequest);
    }
}
