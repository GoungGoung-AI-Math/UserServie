package User.Math.AI.buildUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            // EC2 인스턴스에 접속하여 vault_token.txt 파일 읽기
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "ssh -i ~/Desktop/aikey.pem ec2-user@13.209.22.119 'cat /home/ec2-user/userapp/vault_token.txt'");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String vaultToken = reader.readLine();
            process.waitFor();

            // Vault URL 설정
            // 필요없나?
            String vaultUrl = "https://test.udongrang.com:8200";

            // Vault에서 데이터베이스 사용자 이름, 비밀번호, 구글 클라이언트 아이디와 시크릿 가져오기
            processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "curl -s -k -X GET '" + vaultUrl + "/v1/kv/data/db-creds' -H 'X-Vault-Token: " + vaultToken + "'");
            process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();

            // JSON 파싱
            String jsonResponse = output.toString();
            com.fasterxml.jackson.databind.JsonNode secrets = new com.fasterxml.jackson.databind.ObjectMapper().readTree(jsonResponse);
            String usernameUserDb = secrets.at("/data/data/username_user_db").asText();
            String passwordUserDb = secrets.at("/data/data/password_user_db").asText();

            // 환경 변수 설정
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put("spring.datasource.url", "jdbc:postgresql://13.209.22.119:5434/springdb_user");
            propertyMap.put("spring.datasource.username", usernameUserDb);
            propertyMap.put("spring.datasource.password", passwordUserDb);
            propertyMap.put("spring.datasource.driver-class-name", "org.postgresql.Driver");

            MapPropertySource propertySource = new MapPropertySource("dynamicProperties", propertyMap);
            environment.getPropertySources().addFirst(propertySource);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
