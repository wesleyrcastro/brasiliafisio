package br.com.brasiliaFisio.cursos.converter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class Util {
	public static String formataCPFeCNPJ(String codigo) {
		if (!Util.isEmpty(codigo)) {
			if (codigo.length() == 11) {
				return formataCPF(codigo);
			}
			if (codigo.length() == 8 || codigo.length() == 14) {
				return formataCNPJ(codigo);
			}
		}
		return "";
	}

	public static String formataCPF(String cpf) {
		String cpfFormatado = "";
		if (cpf != null && cpf.length() == 11) {
			int qtd = 0;
			for (char digito : cpf.toCharArray()) {
				cpfFormatado += digito;
				qtd++;
				if (qtd == 3 || qtd == 6) {
					cpfFormatado = cpfFormatado.concat(".");
				}
				if (qtd == 9) {
					cpfFormatado = cpfFormatado.concat("-");
				}
			}
		}
		return cpfFormatado;
	}

	public static String formataCEP(String cep) {
		String cepFormatado = "";
		if (cep != null && cep.length() == 8) {
			int qtd = 0;
			for (char digito : cep.toCharArray()) {
				cepFormatado += digito;
				qtd++;
				if (qtd == 5) {
					cepFormatado = cepFormatado.concat("-");
				}
			}
		}
		return cepFormatado;
	}

	public static String formataCNPJ(String cnpj) {
		String cnpjFormatado = "";
		if (cnpj != null && (cnpj.length() == 14 || cnpj.length() == 8)) {
			int qtd = 0;
			for (char digito : cnpj.toCharArray()) {
				cnpjFormatado += digito;
				qtd++;
				if (qtd == 2 || qtd == 5) {
					cnpjFormatado = cnpjFormatado.concat(".");
				}
				if (cnpj.length() > 8) {
					if (qtd == 8) {
						cnpjFormatado = cnpjFormatado.concat("/");
					}
					if (qtd == 12) {
						cnpjFormatado = cnpjFormatado.concat("-");
					}
				}
			}
		}
		return cnpjFormatado;
	}

	public static String desformataCPF(String cpf) {
		String cpfDesformatado = "";
		if (cpf != null) {
			cpfDesformatado = cpf.replace(".", "").replace("-", "");
		}
		return cpfDesformatado;
	}

	public static String desformataCNPJ(String cnpj) {
		String cnpjDesformatado = "";
		if (cnpj != null) {
			cnpjDesformatado = cnpj.replace(".", "").replace("-", "").replace("/", "");
		}
		return cnpjDesformatado;
	}

	public static String desformataCEP(String cep) {
		String cepDesformatado = "";
		if (cep != null) {
			cepDesformatado = cep.replace("-", "");
		}
		return cepDesformatado;
	}

	public static BigDecimal converteMonetario(String vlr) {
		BigDecimal vlrConvertido = null;
		if (vlr != null) {
			try {
				vlrConvertido = new BigDecimal(vlr.replace(".", "").replace(",", "."));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return vlrConvertido;
	}

	public static String formataMonetario(BigDecimal vlr) {
		if (vlr != null) {
			return NumberFormat.getCurrencyInstance().format(vlr);
		}
		return NumberFormat.getCurrencyInstance().format(0);
	}

	public static String formataPercentual(BigDecimal vlr) {
		if (vlr != null) {
			return NumberFormat.getCurrencyInstance().format(vlr).toString()
					.replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol() + " ", "");
		}
		return NumberFormat.getCurrencyInstance().format(0).toString()
				.replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol() + " ", "");
	}

	public static BigDecimal convertePercentual(String vlr) {
		BigDecimal vlrConvertido = null;
		if (vlr != null) {
			try {
				vlrConvertido = new BigDecimal(vlr.replace(".", "").replace(",", "."));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return vlrConvertido;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object campo) {
		if (campo != null) {
			if (campo instanceof Long) {
				return ((Long) campo).equals(0L);
			}
			if (campo instanceof String) {
				return "".equals(((String) campo).trim()) || "null".equals(campo);
			}
			if (campo instanceof List) {
				return ((List) campo).isEmpty();
			}
			if (campo instanceof Map) {
				return ((Map) campo).entrySet().isEmpty();
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public static boolean isValidoCPF(String cpf) {
		if (cpf.length() == 11) {
			int d1 = 0;
			int d2 = 0;
			int digito1 = 0;
			int digito2 = 0;
			int resto = 0;
			int digitoCPF;
			String nDigResult;
			for (int i = 1; i < cpf.length() - 1; i++) {
				digitoCPF = Integer.valueOf(cpf.substring(i - 1, i)).intValue();

				// Multiplique a ultima casa por 2 a seguinte por 3 a seguinte
				// por 4 e assim por diante.
				d1 = d1 + (11 - i) * digitoCPF;

				// Para o segundo digito repita o procedimento incluindo o
				// primeiro digito calculado no passo anterior.
				d2 = d2 + (12 - i) * digitoCPF;
			}

			// Primeiro resto da divisão por 11.
			resto = d1 % 11;

			// Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é
			// 11 menos o resultado anterior.
			if (resto < 2) {
				digito1 = 0;
			} else {
				digito1 = 11 - resto;
			}
			d2 += 2 * digito1;

			// Segundo resto da divisão por 11.
			resto = d2 % 11;

			// Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é
			// 11 menos o resultado anterior.
			if (resto < 2) {
				digito2 = 0;
			} else {
				digito2 = 11 - resto;
			}

			// Digito verificador do CPF que está sendo validado.
			String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());

			// Concatenando o primeiro resto com o segundo.
			nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

			// Comparar o digito verificador do cpf com o primeiro resto + o
			// segundo resto.
			return nDigVerific.equals(nDigResult);
		}
		return false;
	}

	public static boolean isValidoCNPJ(String cnpj) {
		if (cnpj.length() == 14) {
			int soma = 0;
			int dig = 0;
			String cnpjCalc = cnpj.substring(0, 12);
			char[] chrCnpj = cnpj.toCharArray();

			// Primeira parte
			for (int i = 0; i < 4; i++) {
				if (chrCnpj[i] - 48 >= 0 && chrCnpj[i] - 48 <= 9) {
					soma += (chrCnpj[i] - 48) * (6 - (i + 1));
				}
			}
			for (int i = 0; i < 8; i++) {
				if (chrCnpj[i + 4] - 48 >= 0 && chrCnpj[i + 4] - 48 <= 9) {
					soma += (chrCnpj[i + 4] - 48) * (10 - (i + 1));
				}
			}
			dig = 11 - (soma % 11);
			cnpjCalc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

			// Segunda parte
			soma = 0;
			for (int i = 0; i < 5; i++) {
				if (chrCnpj[i] - 48 >= 0 && chrCnpj[i] - 48 <= 9) {
					soma += (chrCnpj[i] - 48) * (7 - (i + 1));
				}
			}
			for (int i = 0; i < 8; i++) {
				if (chrCnpj[i + 5] - 48 >= 0 && chrCnpj[i + 5] - 48 <= 9) {
					soma += (chrCnpj[i + 5] - 48) * (10 - (i + 1));
				}
			}
			dig = 11 - (soma % 11);
			cnpjCalc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);
			return cnpj.equals(cnpjCalc);
		}
		return false;
	}

	public static String format(Date data, Boolean dataHora) {
		if (dataHora) {
			String format = "";
			if (!Util.isEmpty(data)) {
				return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
			}
			return format;
		}
		return format(data);
	}

	public static String format(Date data) {
		String format = "";
		if (!Util.isEmpty(data)) {
			return new SimpleDateFormat("dd/MM/yyyy").format(data);
		}
		return format;
	}

	public static byte[] toByteArray(File file) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		byte[] b = new byte[(int) file.length()];
		bis.read(b);
		bis.close();
		return b;
	}

	public static byte[] toByteArray(Byte[] bigArray) {
		if (bigArray != null && bigArray.length > 0) {
			byte[] b = new byte[bigArray.length];
			for (int i = 0; i < bigArray.length; i++) {
				b[i] = bigArray[i];
			}
			return b;
		}
		return new byte[0];
	}

	public static String resume(String texto, Integer limiteTamanho) {
		return Util.isEmpty(texto) ? "" : (texto.length() > limiteTamanho ? texto.substring(0, limiteTamanho) + "..."
				: texto);
	}

	public static String converteUtfIso(String utf8) {
		return new String(utf8.getBytes(), Charset.forName("ISO-8859-1"));
	}

	public static void setParametrosQuery(Query query, Map<String, Object> parametros) {
		for (Entry<String, Object> es : parametros.entrySet()) {
			query.setParameter(es.getKey(), es.getValue());
		}
	}

	public static Byte[] toByteArray(byte[] conteudo) {
		Byte[] bigArray = new Byte[conteudo.length];
		for (int i = 0; i < conteudo.length; i++) {
			bigArray[i] = Byte.valueOf(conteudo[i]);
		}
		return bigArray;
	}

	public static boolean validaEmail(String email) {
		boolean isEmailValido = false;
		if (!Util.isEmpty(email)) {
			String expression = "^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				isEmailValido = true;
			}
		}
		return isEmailValido;
	}

	public static String formataMonetarioExtenso(BigDecimal valor) {
		if (Util.isEmpty(valor) || valor.equals(BigDecimal.ZERO)) {
			return ("zero");
		}

		long inteiro = valor.abs().longValue(); // parte inteira do valor
		double resto = valor.subtract(new BigDecimal(inteiro)).doubleValue(); // parte
																				// fracionária
																				// do
																				// valor

		String vlrS = String.valueOf(inteiro);
		if (vlrS.length() > 15)
			return ("Erro: valor superior a 999 trilhões.");

		String s = "", saux, vlrP;
		String centavos = String.valueOf((int) Math.round(resto * 100));

		String[] unidade = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez",
				"onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove" };
		String[] centena = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos",
				"setecentos", "oitocentos", "novecentos" };
		String[] dezena = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta",
				"noventa" };
		String[] qualificaS = { "", "mil", "milhão", "bilhão", "trilhão" };
		String[] qualificaP = { "", "mil", "milhões", "bilhões", "trilhões" };

		// definindo o extenso da parte inteira do valor
		int n, unid, dez, cent, tam, i = 0;
		boolean umReal = false, tem = false;
		while (!vlrS.equals("0")) {
			tam = vlrS.length();
			// retira do valor a 1a. parte, 2a. parte, por exemplo, para
			// 123456789:
			// 1a. parte = 789 (centena)
			// 2a. parte = 456 (mil)
			// 3a. parte = 123 (milhões)
			if (tam > 3) {
				vlrP = vlrS.substring(tam - 3, tam);
				vlrS = vlrS.substring(0, tam - 3);
			} else { // última parte do valor
				vlrP = vlrS;
				vlrS = "0";
			}
			if (!vlrP.equals("000")) {
				saux = "";
				if (vlrP.equals("100"))
					saux = "cem";
				else {
					n = Integer.parseInt(vlrP, 10); // para n = 371, tem-se:
					cent = n / 100; // cent = 3 (centena trezentos)
					dez = (n % 100) / 10; // dez = 7 (dezena setenta)
					unid = (n % 100) % 10; // unid = 1 (unidade um)
					if (cent != 0)
						saux = centena[cent];
					if ((n % 100) <= 19) {
						if (saux.length() != 0)
							saux = saux + " e " + unidade[n % 100];
						else
							saux = unidade[n % 100];
					} else {
						if (saux.length() != 0)
							saux = saux + " e " + dezena[dez];
						else
							saux = dezena[dez];
						if (unid != 0) {
							if (saux.length() != 0)
								saux = saux + " e " + unidade[unid];
							else
								saux = unidade[unid];
						}
					}
				}
				if (vlrP.equals("1") || vlrP.equals("001")) {
					if (i == 0) // 1a. parte do valor (um real)
						umReal = true;
					else
						saux = saux + " " + qualificaS[i];
				} else if (i != 0)
					saux = saux + " " + qualificaP[i];
				if (s.length() != 0)
					s = saux + ", " + s;
				else
					s = saux;
			}
			if (((i == 0) || (i == 1)) && s.length() != 0)
				tem = true; // tem centena ou mil no valor
			i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão,
						// ...
		}

		if (s.length() != 0) {
			if (umReal)
				s = s + " real";
			else if (tem)
				s = s + " reais";
			else
				s = s + " de reais";
		}

		// definindo o extenso dos centavos do valor
		if (!centavos.equals("0")) { // valor com centavos
			if (s.length() != 0) // se não é valor somente com centavos
				s = s + " e ";
			if (centavos.equals("1"))
				s = s + "um centavo";
			else {
				n = Integer.parseInt(centavos, 10);
				if (n <= 19)
					s = s + unidade[n];
				else { // para n = 37, tem-se:
					unid = n % 10; // unid = 37 % 10 = 7 (unidade sete)
					dez = n / 10; // dez = 37 / 10 = 3 (dezena trinta)
					s = s + dezena[dez];
					if (unid != 0)
						s = s + " e " + unidade[unid];
				}
				s = s + " centavos";
			}
		}
		return (s);
	}
}
