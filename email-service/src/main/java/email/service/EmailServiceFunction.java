package email.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.MessageRequest;
import io.micronaut.context.annotation.Value;
import io.micronaut.function.executor.FunctionInitializer;
import io.micronaut.function.FunctionBean;
import javax.inject.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

@FunctionBean("email-service")
public class EmailServiceFunction extends FunctionInitializer implements Function<MessageRequest, String> {

    @Value("${cloud.aws.email.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public String apply(MessageRequest msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mapper.writeValue(baos, msg);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("application/json");
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .build();
            s3.putObject(bucketName,
                    String.valueOf(msg.hashCode()),
                    bais,
                    objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed "+ e.getMessage();
        }

         return "Success";
    }

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
     * where the argument to echo is the JSON to be parsed.
     */
    public static void main(String...args) throws IOException {
        EmailServiceFunction function = new EmailServiceFunction();
        function.run(args, (context)-> function.apply(context.get(MessageRequest.class)));
    }    
}

