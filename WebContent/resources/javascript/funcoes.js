PrimeFaces.locales['pt_BR'] = {
	closeText : 'Fechar',
	prevText : 'Anterior',
	nextText : 'Próximo',
	currentText : 'Começo',
	monthNames : [ 'Janeiro', 'Fevereiro', 'Marco', 'Abril', 'Maio', 'Junho',
			'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro' ],
	monthNamesShort : [ 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago',
			'Set', 'Out', 'Nov', 'Dez' ],
	dayNames : [ 'Domingo', 'Segunda', 'Terca', 'Quarta', 'Quinta', 'Sexta',
			'Sabado' ],
	dayNamesShort : [ 'Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sab' ],
	dayNamesMin : [ 'D', 'S', 'T', 'Q', 'Q', 'S', 'S' ],
	weekHeader : 'Semana',
	firstDay : 1,
	isRTL : false,
	showMonthAfterYear : false,
	yearSuffix : '',
	timeOnlyTitle : 'So Horas',
	timeText : 'Tempo',
	hourText : 'Hora',
	minuteText : 'Minuto',
	secondText : 'Segundo',
	currentText : 'Data de Hoje',
	ampm : false,
	month : 'Mes',
	week : 'Semana',
	day : 'Dia',
	allDayText : 'Todo Dia'
};

function apenasNumerosPonto(campo) {
	campo.value = campo.value.replace(/[^0-9.]+/, '');
}

function apenasNumerosVirgula(campo) {
	campo.value = campo.value.replace(/[^0-9,]+/, '');
}

function apenasNumeros(campo) {
	campo.value = campo.value.replace(/[^0-9]+/, '');
}

function apenasLetra(campo) {
	campo.value = campo.value.replace(/[^a-z\sçãáéíóêúô]+/, '');
}

function my_onkeydown_handler(event){
    switch (event.keyCode) {          
     case 116 : // 'F5'
           event.returnValue = false;
           event.keyCode = 0;
           window.status = "Atualização desabilitada (F5)";
           alert("Operação desabilitada : F5");
           return false; 
           break; 
    case 17 : // Ctrl
      keyUpTag = false;
      break;
    case 82 : // R 
      if (keyUpTag) {
           event.returnValue = false;
           event.keyCode = 0;
           window.status = "Atualização desabilitada (Ctrl + R)";
           alert("Operação desabilitada : Ctrl + R");
           return false; 
      }
      break;
    case 114 : // r 
      if (keyUpTag) {
           event.returnValue = false;
           event.keyCode = 0;
           window.status = "Atualização desabilitada (Ctrl + r)";
           alert("Operação desabilitada : Ctrl + r");
           return false; 
      }
      break;
    }
}

function getDadosEndereco() {
	var cep = $("#cep").val().replace(/[^\d]/g, '');
	$.ajax({
		url : "https://viacep.com.br/ws/"+ cep +"/json/",
		type : 'GET',
		dataType : 'json',
		success : function(json) {
			$("#endereco").val(json.logradouro);
			$('#bairro').val(json.bairro);
			$("#cidade").val(json.localidade);
			
			$('option', '#estado_input').each(function() {
				var opt = $(this);
				if(opt.val() == json.uf) {
					opt.prop('selected', true);
					opt.parents('#estado').children('.ui-selectonemenu-trigger').trigger('click').trigger('click');
				}
			});
		},
	});
}


jQuery(document).ready(function(jQuery) {  
    // Chamada da funcao upperText(); ao carregar a pagina  
    upperText();  
});  
// Funcao que faz o texto ficar em uppercase  
function upperText() {  
    // Para tratar o colar  
    jQuery(".up").bind('paste', function(e) {  
        var el = jQuery(this);  
        setTimeout(function() {  
            var text = jQuery(el).val();  
            el.val(text.toUpperCase());  
        }, 100);  
    });  

    if ( jQuery.browser.mozilla) {  
    	// Para tratar quando é digitado    
    	jQuery(".up").keyup(function() {    
    	        var el = jQuery(this);    
    	        setTimeout(function() {    
    	            var text = jQuery(el).val();    
    	            el.val(text.toUpperCase());    
    	        }, 100);    
    	    });  
    	}  
    	  
    	if ( jQuery.browser.msie || jQuery.browser.safari||jQuery.browser.chrome) {  
    	// Para tratar quando é digitado    
    	    jQuery(".up").keypress(function() {    
    	        var el = jQuery(this);    
    	        setTimeout(function() {    
    	            var text = jQuery(el).val();    
    	            el.val(text.toUpperCase());    
    	        }, 100);    
    	    });  
    	} 
}

jQuery(document).ready(function(jQuery) {  
    // Chamada da funcao upperText(); ao carregar a pagina  
	lowerText();  
});  
// Funcao que faz o texto ficar em uppercase  
function lowerText() {  
    // Para tratar o colar  
    jQuery(".lo").bind('paste', function(e) {  
        var el = jQuery(this);  
        setTimeout(function() {  
            var text = jQuery(el).val();  
            el.val(text.toLowerCase());  
        }, 100);  
    });  

    if ( jQuery.browser.mozilla) {  
    	// Para tratar quando é digitado    
    	jQuery(".lo").keyup(function() {    
    	        var el = jQuery(this);    
    	        setTimeout(function() {    
    	            var text = jQuery(el).val();    
    	            el.val(text.toLowerCase());    
    	        }, 100);    
    	    });  
    	}  
    	  
    	if ( jQuery.browser.msie || jQuery.browser.safari||jQuery.browser.chrome) {  
    	// Para tratar quando é digitado    
    	    jQuery(".lo").keypress(function() {    
    	        var el = jQuery(this);    
    	        setTimeout(function() {    
    	            var text = jQuery(el).val();    
    	            el.val(text.toLowerCase());    
    	        }, 100);    
    	    });  
    	} 
}