package br.com.valueprojects.mock_spring;

public class SMSService {
    String vencedor = null;
	public void enviar(Object object) {

		System.out.println("SMS enviado para o vencedor: " + vencedor);
	}

}
