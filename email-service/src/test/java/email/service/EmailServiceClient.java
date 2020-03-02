package email.service;

import com.prapps.aws.email.MessageRequest;
import io.micronaut.function.client.FunctionClient;
import io.micronaut.http.annotation.Body;
import io.reactivex.Single;
import javax.inject.Named;

@FunctionClient
public interface EmailServiceClient {

    @Named("email-service")
    Single<String> apply(@Body MessageRequest body);

}
