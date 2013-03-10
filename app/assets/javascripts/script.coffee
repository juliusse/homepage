$ ->
  console.log "Initializing..."
  
  activeLi = $('ul.navigation').find('a[href="'+window.location.pathname+'"]').parent()
  activeLi.addClass('active')
  activeLi.find('ul').removeClass("invisible")
  activeLi.parent().removeClass("invisible")
