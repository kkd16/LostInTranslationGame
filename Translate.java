import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClientBuilder;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;

public class AmazonTranslateExample {

  public static void main(String[] args) {
    String accessKey = "AKIA4SBPTUWC4SKBWWO4";
    String secretKey = "U5zSyejkzWpjFk2/YjUFPrNzYgIHERu79IRK9ql4";
    String region = "ca-central-1"; // for example, "us-west-2"

    BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

    AmazonTranslate translate = AmazonTranslateClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .withRegion(Regions.fromName(region))
        .build();

    TranslateTextRequest request = new TranslateTextRequest()
        .withText("Hello, world!")
        .withSourceLanguageCode("en")
        .withTargetLanguageCode("fr");

    TranslateTextResult result = translate.translateText(request);

    System.out.println(result.getTranslatedText());
  }
}