package email.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.micronaut.context.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class S3MessageSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceFunction.class);

    @Value("${cloud.aws.email.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    public void addFile(String key, String contentType, byte[] content) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .build();
        LOG.debug("acquired s3 connection with  region % and bucket %s", region, bucketName);
        s3.putObject(bucketName,
                String.valueOf(content.hashCode()),
                bais,
                objectMetadata);
    }
}
