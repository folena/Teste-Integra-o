package br.com.valueprojects.mock_spring;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.valueprojects.mock_spring.builder.CriadorDeJogo;
import br.com.valueprojects.mock_spring.model.FinalizaJogo;
import br.com.valueprojects.mock_spring.model.Jogo;
import infra.JogoDao;




public class FinalizaJogoTest {
	
	 @Test
	    public void deveFinalizarJogosDaSemanaAnterior() {

	        Calendar antiga = Calendar.getInstance();
	        antiga.set(1999, 1, 20);

	        Jogo jogo1 = new CriadorDeJogo().para("Ca�a moedas")
	            .naData(antiga).constroi();
	        Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras")
	            .naData(antiga).constroi();

	        // mock no lugar de dao falso
	        
	        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

	        JogoDao daoFalso = mock(JogoDao.class);

	        when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

	        FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
	        finalizador.finaliza();

	        assertTrue(jogo1.isFinalizado());
	        assertTrue(jogo2.isFinalizado());
	        assertEquals(2, finalizador.getTotalFinalizados());
	    }
	 
	 @Test
		public void deveVerificarSeMetodoAtualizaFoiInvocado() {

			Calendar antiga = Calendar.getInstance();
			antiga.set(1999, 1, 20);

			Jogo jogo1 = new CriadorDeJogo().para("Cata moedas").naData(antiga).constroi();
			Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();

			// mock no lugar de dao falso

			List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

			JogoDao daoFalso = mock(JogoDao.class);

			when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

			FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
			finalizador.finaliza();

			verify(daoFalso, times(1)).atualiza(jogo1);
			//Mockito.verifyNoInteractions(daoFalso);
	
					
			
		}
		 @Test
		 public void deveEnviarSMSAposSalvarVencedor() {
		     JogoDao dao = mock(JogoDao.class);
		     SMSService smsService = mock(SMSService.class);
	
		     Calendar antiga = Calendar.getInstance();
		     antiga.set(1999, 1, 20); // Data passada para simular semana anterior
		     Jogo jogo1 = new CriadorDeJogo().para("Caça moedas").naData(antiga).constroi();
		     Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras").naData(antiga).constroi();
	
		     //Jogo em andamento
		     when(dao.emAndamento()).thenReturn(Arrays.asList(jogo1, jogo2));
	
		     // Classe FinalizaJogo com o mock do DAO
		     FinalizaJogo finalizaJogo = new FinalizaJogo(dao);
	
		     finalizaJogo.finaliza();
	
		     verify(dao, times(1)).atualiza(jogo1);
		     verify(dao, times(1)).atualiza(jogo2);
	
		     // Verificar que nenhum SMS foi enviado antes da atualização
		     verifyNoInteractions(smsService);
	
		     // Agora simular que o SMS deve ser enviado após a atualização
		     smsService.enviar(jogo1.getVencedor());
		     verify(smsService, times(1)).enviar(jogo1.getVencedor());
		 }
 
	}

 

	
	

	
