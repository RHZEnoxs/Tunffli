$( document ).ready(function() {
    /*$(window).scroll(function(){
		if ($(this).scrollTop() > 100) {
			$('#scrollToTop').fadeIn();
		} else {
			$('#scrollToTop').fadeOut();
		}
	});*/
	
	//Click event to scroll to top
	$('#btn-generating').click(function(){
		$('html, body').animate({scrollTop : window.innerHeight-70},500);
		return false;
	});
    $("#rsa-view").height(window.innerHeight);
    
});