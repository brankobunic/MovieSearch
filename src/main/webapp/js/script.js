$(function() {
	$("#search").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "",
				method : "POST",
				data : {
					term : request.term
				},
				success : function(data) {
					response(data);
				}
			});
		},
		minLength : 2,
		delay : 100,
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