package email.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.MessageRequest;
import com.prapps.aws.email.MimeMessageRequest;
import io.micronaut.context.annotation.Value;
import io.micronaut.function.FunctionBean;
import io.micronaut.function.executor.FunctionInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

@FunctionBean("email-service")
public class EmailServiceFunction extends FunctionInitializer implements Function<MimeMessageRequest, String> {
    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceFunction.class);
    @Value("${cloud.aws.email.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Override
    public String apply(MimeMessageRequest msg) {
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
            LOG.debug("acquired s3 connection with  region % and bucket %s", region, bucketName);
            s3.putObject(bucketName,
                    String.valueOf(msg.hashCode()),
                    bais,
                    objectMetadata);
        } catch (IOException e) {
            LOG.error("exception while putting object in bucket %s", bucketName,e);
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
        function.run(args, (context)-> function.apply(context.get(MimeMessageRequest.class)));
    }    
}

