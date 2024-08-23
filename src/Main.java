import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.Gson;

class Endereco {
    String resultado;
    String resultado_txt;
    String uf;
    String cidade;
    String bairro;
    String tipo_logradouro;
    String logradouro;
}

public class Main {
    public static void main(String[] args) {

        String cep;
        Scanner teclado = new Scanner(System.in);
        System.out.println("Digite o CEP que deseja descobrir o endereço. ");

        while (true) {
            System.out.print("(apenas números, 8 dígitos): ");
            cep = teclado.nextLine();

            // Valida o cep
            if (cep.matches("\\d{8}")) {
                break;
            } else {
                System.out.println("CEP inválido! O CEP deve conter exatamente 8 dígitos numéricos.");
            }
        }

        try {
            // Cria cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Cria requisição
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://cep.republicavirtual.com.br/web_cep.php?cep="+cep+"&formato=json"))
                    .GET() // Método de requisição GET
                    .build();

            // Envia a requisição e recebe a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Criando um objeto Gson (lib)
            Gson gson = new Gson();

            // Converte string JSON para objeto Java
            Endereco endereco = gson.fromJson(response.body(), Endereco.class);

            // Printa os dados
            boolean cepValido = endereco.resultado.equals("1");

            if(cepValido) {
                System.out.println("----- Seu Endereço -----");
                System.out.println("Estado: " + endereco.uf);
                System.out.println("Cidade: " + endereco.cidade);
                System.out.println("Bairro: " + endereco.bairro);
                System.out.println(endereco.tipo_logradouro + " " + endereco.logradouro);
                System.out.println("------------------------");
            } else {
                System.out.println("------------------------");
                System.out.println("Endereço não Encontrado.");
                System.out.println("------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
            }
    }
}

