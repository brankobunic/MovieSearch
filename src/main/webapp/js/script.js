$(function() {
	$("#search").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "MainServlet",
				method : "GET",
				data : {
					movieSearch : request.term
				},
				success : function(data) {
					response(data);
				},
				error: function(jqXHR,textStatus,errorThrown ) {
			          alert("There was an error. Try again please!", errorThrown);
		        }
			});
		},
		minLength : 2,
		delay : 300,
		focus : function() {
			return false;
		},
		select : function(event, ui) {
			let terms = this.value.split(/\s+/);
			terms.pop();
			terms.push(ui.item.value.trim());
			this.value = terms.join(" ");
			return false;
		}
	});
});