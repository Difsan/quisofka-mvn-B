package co.com.quisofka.quizzes.infrastructure.drivenAdapters;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.email.gateways.EmailReposiroty;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AdapterEmail implements EmailReposiroty {

    private JavaMailSender mailSender;
    private MailProperties mailProperties;
    private ResourceLoader resourceLoader;

    @Override
    public Mono<Void> sendQuizCodeByEmail(Email email, String quizCode) {
        return Mono.fromRunnable(() -> {
            try {
                // Load the HTML template from the classpath
                System.out.println("entrando a sendHtmlEmail");
                Resource resource = new ClassPathResource("src/main/java/co/com/quisofka/quizzes/infrastructure/drivenAdapters/util/template/codeSentEmail-Template.html");
                System.out.println("guardando resource");
                System.out.println(resource);
                byte[] contentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String htmlBody = new String(contentBytes, "UTF-8");
                System.out.println("imprimiendo htmlBody");
                System.out.println(htmlBody);

                // Replace placeholders in the HTML template with dynamic values
                htmlBody = htmlBody.replace("[[name]]", email.getStudentName());

                MimeMessage message = mailSender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom("quissofka@gmail.com");
                helper.setTo(email.getTo());
                helper.setSubject(email.getMessageSubject());
                helper.setText(htmlBody, true);

                mailSender.send(message);
            } catch (Exception ex) {
                throw new RuntimeException( ex);
            }
        });
    }

    @Override
    public Mono<Void> sendStudentResultByEmail(Email email) {
        return null;
    }
}
