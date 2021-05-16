package br.com.brasiliaFisio.cursos.util.report;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.brasiliaFisio.cursos.util.service.NegocioException;

public class UtilRelatoriosLista {
	
	private static Log log = LogFactory.getLog(UtilRelatoriosLista.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void imprimeRelatorio(String relatorioNome, HashMap parametros, List lista) {
		try {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String path = scontext.getRealPath("/WEB-INF/relatorios/");
			parametros.put("SUBREPORT_DIR", path + File.separator);
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/relatorios")
					+ File.separator + relatorioNome + ".jasper", parametros, dataSource);

			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);
			HttpServletResponse res = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			res.setContentType("application/pdf");
			int codigo = (int) (Math.random() * 1000);
			res.setHeader("Content-disposition", "inline);filename=Relatorio_" + codigo + ".pdf");
			res.getOutputStream().write(b);
			res.getCharacterEncoding();
			facesContext.responseComplete();

		} catch (Exception e) {
			log.error("Erro de relatório: " + e.getMessage());
			throw new NegocioException("Falha ao imprimir o PDF!");
		}

	}
}
